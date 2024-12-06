import {
  signInWithEmailAndPassword,
  createUserWithEmailAndPassword,
  updateProfile,
  signOut,
  signInWithPopup,
  deleteUser,
  setPersistence,
  browserLocalPersistence
} from "firebase/auth";
import { ref as firebaseRef, uploadBytes, getDownloadURL } from "firebase/storage";
import { auth, storage, googleProvider } from "../assets/js/firebase";
import ApiService from "@/services/ApiService";
import { useRouter } from "vue-router";
import { defineStore } from "pinia";
import { ref } from "vue";

export const useUserStore = defineStore("user", () => {
  const user = ref(auth.currentUser);
  const router = useRouter();

  const register = async (email, password, displayName) => {
    try {
      const createdUser = await createUserWithEmailAndPassword(
        auth,
        email,
        password,
      );
  
      await updateProfile(auth.currentUser, { displayName });
  
      await ApiService.post("/users", { uid: auth.currentUser.uid })
        .then(() => router.push("/posts"))
        .catch(async () => {
          await deleteUser(createdUser);
        });
    } catch(error) {
      throw new Error("Une erreur est survenue. Veuillez réessayer.");
    }
  };

  const loginWithGoogle = async () => {
    try {
      await setPersistence(auth, browserLocalPersistence);
  
      await signInWithPopup(auth, googleProvider);
  
      await ApiService.get(`/users/${user.value.uid}`)
        .then(() => {
          router.push("/posts");
        })
        .catch(async (error) => {
          if (error.status === 404) {
            await ApiService.post("/users", { uid: auth.currentUser.uid });
            router.push("/posts");
          }
        });
    } catch(error) {
      console.log(error);
      throw new Error("Une erreur est survenue. Veuillez réessayer.");
    }
  };

  const login = async (email, password, remember) => {
    if (remember) {
      await setPersistence(auth, browserLocalPersistence);
    }
    
    try {
      await signInWithEmailAndPassword(auth, email, password);
      router.push("/posts");
    } catch(error) {
      if (error.code === "auth/invalid-credential") {
        throw new Error("Informations de connexion incorrectes.");
      }
      else {
        throw new Error("Une erreur est survenue. Veuillez réessayer.");
      }
    }
  };

  const updateUser = async () => {
    try {

    } catch(error) {}
  };

  const saveProfilePicture = async (file) => {
    if (!file) throw new Error("No file provided");

    try {
      const fileRef = firebaseRef(storage, `profile_pictures/${auth.currentUser.uid}/${file.name}`);

      await uploadBytes(fileRef, file);
      const downloadURL = await getDownloadURL(fileRef);

      await updateProfile(auth.currentUser, { photoURL: downloadURL });
    } catch (error) {
      throw new Error("Erreur lors de la mise à jour de la photo de profil");
    }
  };

  const deleteUser = async () => {
    try {

    } catch(error) {}
  }

  const logout = async () => {
    signOut(auth).then(() => {
      router.push("/login");
    });
  };

  return { user, login, logout, register, loginWithGoogle, updateUser, saveProfilePicture };
});
