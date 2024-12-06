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
import { auth, googleProvider } from "../assets/js/firebase";
import ApiService from "@/services/ApiService";
import { useRouter } from "vue-router";
import { defineStore } from "pinia";
import { ref } from "vue";

export const useUserStore = defineStore("user", () => {
  const user = ref(auth.currentUser);
  const uid = ref(auth.currentUser.uid || '');
  const router = useRouter();

  const register = async (email, password, displayName) => {
    await setPersistence(auth, browserLocalPersistence);

    const createdUser = await createUserWithEmailAndPassword(
      auth,
      email,
      password,
    );

    await updateProfile(auth.currentUser, { displayName });

    await ApiService.post("/users", { uid: auth.currentUser.uid })
      .then(() =>{
        uid.value = auth.currentUser.uid;
        router.push("/posts");
      })

      .catch(async () => {
        await deleteUser(createdUser);
      });
  };

  const loginWithGoogle = async () => {
    await setPersistence(auth, browserLocalPersistence);

    await signInWithPopup(auth, googleProvider);

    await ApiService.get(`/users/${user.value.uid}`)
      .then(() => {
        uid.value = auth.currentUser.uid;
        router.push("/posts");
      })
      .catch(async (error) => {
        if (error.status === 404) {
          await ApiService.post("/users", { uid: auth.currentUser.uid });
          router.push("/posts");
        }
      });
  };

  const login = async (email, password) => {
    await setPersistence(auth, browserLocalPersistence);
    
    await signInWithEmailAndPassword(auth, email, password);
    uid.value = auth.currentUser.uid;
    router.push("/posts");
  };

  const logout = async () => {
    signOut(auth).then(() => {
      router.push("/login");
      uid.value = '';
    });
  };

  return { user, uid, login, logout, register, loginWithGoogle };
});
