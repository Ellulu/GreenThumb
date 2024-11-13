import {
  signInWithEmailAndPassword,
  createUserWithEmailAndPassword,
  updateProfile,
  signOut,
  signInWithPopup,
  deleteUser,
} from "firebase/auth";
import { auth, googleProvider } from "../assets/js/firebase";
import ApiService from "@/services/ApiService";
import { useRouter } from "vue-router";
import { defineStore } from "pinia";
import { ref } from "vue";

export const useUserStore = defineStore("user", () => {
  const user = ref({
    displayName: "",
    email: "",
    uid: "",
  });
  const isLoggedIn = ref(false);
  const router = useRouter();

  const register = async (email, password, displayName) => {
    const createdUser = await createUserWithEmailAndPassword(
      auth,
      email,
      password
    );
    console.log(createdUser);

    await updateProfile(auth.currentUser, { displayName }).then(() => {
      user.value = {
        displayName: displayName,
        email: email,
        uid: createdUser.user.uid,
      };
      isLoggedIn.value = true;
    });

    await ApiService.post("/users", { uid: user.value.uid })
      .then(() => router.push("/dashboard"))
      .catch(async () => {
        await deleteUser(createdUser);
      });
  };

  const loginWithGoogle = async () => {
    await signInWithPopup(auth, googleProvider).then((userCredential) => {
      const signedInUser = userCredential.user;
      user.value = {
        displayName: signedInUser.displayName,
        email: signedInUser.email,
        uid: signedInUser.uid,
      };
      isLoggedIn.value = true;
    });

    await ApiService.get(`/users/${user.value.uid}`)
      .then(() => {
        router.push("/dashboard");
      })
      .catch(async (error) => {
        if (error.status === 404) {
          await ApiService.post("/users", { uid: user.value.uid });
          router.push("/dashboard");
        }
      });
  };

  const login = async (email, password) => {
    signInWithEmailAndPassword(auth, email, password).then((userCredential) => {
      const signedInUser = userCredential.user;
      user.value = {
        displayName: signedInUser.displayName,
        email: signedInUser.email,
        uid: signedInUser.uid,
      };
      isLoggedIn.value = true;
      router.push("/dashboard");
    });
  };

  const logout = async () => {
    signOut(auth).then(() => {
      user.value = {
        displayName: "",
        email: "",
        uid: "",
      };
      isLoggedIn.value = false;
      router.push("/login");
    });
  };

  return { user, isLoggedIn, login, logout, register, loginWithGoogle };
});
