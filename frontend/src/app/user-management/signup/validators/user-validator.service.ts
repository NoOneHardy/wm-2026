import {inject, Injectable} from '@angular/core'
import {UserService} from '../../user.service'
import {AbstractControl, AsyncValidatorFn} from '@angular/forms'
import {debounceTime, distinctUntilChanged, first, map, switchMap} from 'rxjs'

@Injectable({
  providedIn: 'root'
})
export class UserValidatorService {
  private userService = inject(UserService)

  usernameAvailable(): AsyncValidatorFn {
    return (control: AbstractControl) => {
      return control.valueChanges.pipe(
        debounceTime(500),
        distinctUntilChanged(),
        switchMap(v => this.userService.checkUsername(v)),
        map(check => {
          if (check.isUsernameAvailable) return null
          return {usernameAvailable: true}
        }),
        first()
      )
    }
  }

  emailAvailable(): AsyncValidatorFn {
    return (control: AbstractControl) => {
      return control.valueChanges.pipe(
        debounceTime(500),
        distinctUntilChanged(),
        switchMap(v => this.userService.checkEmail(v)),
        map(check => {
          if (check.isEmailAvailable) return null
          return {emailAvailable: true}
        }),
        first()
      )
    }
  }
}
