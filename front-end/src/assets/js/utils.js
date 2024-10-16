export function isEmailValid(email) {
    const emailRegex = /^[^\s@]+@[^\s@]+\.[^\s@]+$/;
    return emailRegex.test(email);
}

export const validatePassword = (value) => {
    const errors = []
    if (value.length < 8) {
      errors.push('au moins 8 caractères')
    }
    if (!/[A-Z]/.test(value)) {
      errors.push(' une majuscule')
    }
    if (!/[0-9]/.test(value)) {
      errors.push(' un chiffre')
    }
    if (!/[!@#$%^&*(),.?":{}|<>]/.test(value)) {
      errors.push(' un caractère spécial')
    }
    return errors
  }

export function isPasswordConfirmationValid(password, confirmPassword) {
    return password === confirmPassword;
}