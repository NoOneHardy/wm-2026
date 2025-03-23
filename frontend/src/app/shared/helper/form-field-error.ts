import {AbstractControl} from '@angular/forms'

export function hasError(control: AbstractControl, error?: string): boolean {
   return control.errors && control.touched && (!error || control.errors[error])
 }
