import {
  signInWithEmailAndPassword,
  createUserWithEmailAndPassword,
  updateProfile,
  signOut,
  signInWithPopup,
  deleteUser as firebaseDeleteUser,
  setPersistence,
  browserLocalPersistence,
  onAuthStateChanged,
} from "firebase/auth";
import { auth, googleProvider } from "../assets/js/firebase";
import ApiService from "@/services/ApiService";

import { useRouter } from "vue-router";
import { defineStore } from "pinia";
import { ref } from "vue";

export const useUserStore = defineStore("user", () => {
  const user = ref(null);
  const isInitialized = ref(false);
  const router = useRouter();

  const initializeUser = async () => {
    if (isInitialized.value) return;
    return new Promise((resolve) => {
      onAuthStateChanged(auth, (authUser) => {
        user.value = authUser || null;
        isInitialized.value = true;
        resolve();
      });
    });
  };

  const register = async (email, password, displayName) => {
    try {
      const createdUser = await createUserWithEmailAndPassword(auth, email, password);
      await updateProfile(auth.currentUser, { displayName });
      await ApiService.post("/users", { uid: auth.currentUser.uid })
        .then(() => router.push("/posts"))
        .catch(async () => {
          await firebaseDeleteUser(createdUser);
        });
    } catch (error) {
      throw new Error("Une erreur est survenue. Veuillez réessayer.");
    }
  };

  const loginWithGoogle = async () => {
    try {
      await setPersistence(auth, browserLocalPersistence);
      await signInWithPopup(auth, googleProvider);

      await ApiService.get(`/users/${auth.currentUser.uid}`)
        .then(() => {
          router.push("/posts");
        })
        .catch(async (error) => {
          if (error.status === 404) {
            await ApiService.post("/users", { uid: auth.currentUser.uid });
            router.push("/posts");
          }
        });
    } catch (error) {
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
    } catch (error) {
      if (error.code === "auth/invalid-credential") {
        throw new Error("Informations de connexion incorrectes.");
      } else {
        throw new Error("Une erreur est survenue. Veuillez réessayer.");
      }
    }
  };

  const saveProfilePicture = async (file) => {
    if (!file) throw new Error("No file provided");

    try {
      await ApiService.post('/users/upload', file, true);
    } catch (error) {
      throw new Error("Erreur lors de la mise à jour de la photo de profil");
    }
  };

  const logout = async () => {
    await signOut(auth);
    user.value = null;
    router.push("/login");
  };

  return {
    user,
    isInitialized,
    initializeUser,
    login,
    logout,
    register,
    loginWithGoogle,
    saveProfilePicture,
  };
});
