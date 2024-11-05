import { signInWithEmailAndPassword, createUserWithEmailAndPassword, updateProfile, signOut, signInWithPopup } from 'firebase/auth'
import { auth, googleProvider } from '../assets/js/firebase'
import { useRouter } from 'vue-router'
import { defineStore } from 'pinia'
import { ref } from 'vue'

export const useUserStore = defineStore('user', () => {
    const user = ref({
        displayName: '',
        email: '',
    })
    const isLoggedIn = ref(false)
    const router = useRouter()

    const register = async(email, password, displayName) => {
        await createUserWithEmailAndPassword(auth, email, password)
        
        updateProfile(auth.currentUser, { displayName: displayName })
        .then(() => {
            user.value.displayName = displayName
            user.value.email = email
            isLoggedIn.value = true
            router.push('/dashboard')
        })
    }

    const loginWithGoogle = async() => {
        signInWithPopup(auth, googleProvider)
        .then((userCredential) => {
            const SignedInUser = userCredential.user
            user.value.displayName = SignedInUser.displayName
            user.value.email = SignedInUser.email
            isLoggedIn.value = true
            router.push('/dashboard')
        })
    }

    const login = async(email, password) => {
        signInWithEmailAndPassword(auth, email, password).then((userCredential) => {
            const SignedInUser = userCredential.user
            user.value.displayName = SignedInUser.displayName
            user.value.email = SignedInUser.email
            isLoggedIn.value = true
            router.push('/dashboard')
        })
    }

    const logout = async() => {
        signOut(auth)
        .then(() => {
            user.value = {
                displayName: '',
                email: '',
            }
            isLoggedIn.value = false
            router.push('/login')
        })
    }

    return { user, isLoggedIn, login, logout, register, loginWithGoogle }
})