import {
  signInWithEmailAndPassword,
  createUserWithEmailAndPassword,
  updateProfile,
  signOut,
  signInWithPopup,
  deleteUser,
  setPersistence,
  browserLocalPersistence,
  onAuthStateChanged
} from "firebase/auth";
import { auth, googleProvider } from "../assets/js/firebase";
import ApiService from "@/services/ApiService";

import { useRouter } from "vue-router";
import { defineStore } from "pinia";
import { ref } from "vue";

export const useUserStore = defineStore("user", () => {
  const user = ref(null);
  const router = useRouter();

  // Écouter les changements d'état de connexion et mettre à jour le store
  onAuthStateChanged(auth, (authUser) => {
    if (authUser) {
      user.value = authUser;
    } else {
      user.value = null;
    }
  });

  const register = async (email, password, displayName) => {
    await setPersistence(auth, browserLocalPersistence);

    const createdUser = await createUserWithEmailAndPassword(
      auth,
      email,
      password
    );

    await updateProfile(auth.currentUser, { displayName });

    await ApiService.post("/users", { uid: auth.currentUser.uid })
      .then(() => router.push("/posts"))
      .catch(async () => {
        await deleteUser(createdUser);
      });
  };

  const loginWithGoogle = async () => {
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
  };

  const login = async (email, password) => {
    await setPersistence(auth, browserLocalPersistence);
    await signInWithEmailAndPassword(auth, email, password);
    user.value = auth.currentUser; // Mise à jour de l'utilisateur après connexion
    router.push("/posts");
  };

  const logout = async () => {
    await signOut(auth);
    user.value = null;
    router.push("/login");
  };

  return { user, login, logout, register, loginWithGoogle };
});
