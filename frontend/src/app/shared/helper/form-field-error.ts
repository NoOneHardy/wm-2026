import {AbstractControl} from '@angular/forms'

export function hasError(control: AbstractControl, error?: string): boolean {
  if (control.untouched || !control.errors) return false

  if (error) {
    const index = Object.keys(control.errors).indexOf(error)
    if (index != 0) return false
  }
  return control.invalid
}
