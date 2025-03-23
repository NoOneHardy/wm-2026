import {ValidatorFn} from '@angular/forms'

export function passwordMatch(): ValidatorFn {
  return (group) => {

    const password = group.get('password')
    const confirmPassword = group.get('confirmPassword')

    if (!password || !confirmPassword) return null

    if (password.value !== confirmPassword.value) return {passwordMatch: true}
    return null
  }
}
