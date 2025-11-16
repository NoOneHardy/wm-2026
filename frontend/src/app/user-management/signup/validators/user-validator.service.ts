import {inject, Injectable} from '@angular/core'
import {UserService} from '../../user.service'
import {AbstractControl, AsyncValidatorFn} from '@angular/forms'
import {first, map, of, switchMap, timer} from 'rxjs'
import {Store} from '@ngrx/store'
import {selectUser} from '../../store/user.feature'

@Injectable({
  providedIn: 'root'
})
export class UserValidatorService {
  private userService = inject(UserService)
  private store = inject(Store)
  private currentUser = this.store.selectSignal(selectUser)

  usernameAvailable(): AsyncValidatorFn {
    return (control: AbstractControl) => {
      const currentUsername = this.currentUser()?.username
      const inputUsername = control.value

      if (currentUsername === inputUsername) return of(null)
      return timer(500).pipe(
        map(() => inputUsername),
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
      const currentEmail = this.currentUser()?.email
      const inputEmail = control.value

      if (currentEmail === inputEmail) return of(null)
      return timer(500).pipe(
        map(() => inputEmail),
        switchMap((v) => this.userService.checkEmail(v)),
        map(check => {
          if (check.isEmailAvailable) return null
          return {emailAvailable: true}
        }),
        first()
      )
    }
  }
}
