import { initializeApp } from "firebase/app";
import {
  getAuth,
  GoogleAuthProvider,
  FacebookAuthProvider,
} from "firebase/auth";
import { getStorage } from "firebase/storage";

const firebaseConfig = {
  apiKey: "AIzaSyAqL9gREXGzgjB-1k9VuDWNHHYeRWOaSaY",
  authDomain: "greenthumb-54c99.firebaseapp.com",
  projectId: "greenthumb-54c99",
  storageBucket: "greenthumb-54c99.firebasestorage.app",
  messagingSenderId: "977712179231",
  appId: "1:977712179231:web:4defe8eca3184d3d410d2a",
  measurementId: "G-Y7FTQ71DCB",
};

const firebaseApp = initializeApp(firebaseConfig);
export const auth = getAuth(firebaseApp);
export const storage = getStorage(firebaseApp);

export const googleProvider = new GoogleAuthProvider();
export const facebookProvider = new FacebookAuthProvider();
