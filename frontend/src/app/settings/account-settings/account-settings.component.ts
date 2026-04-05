import {Component, inject, Signal} from '@angular/core'
import {AvatarUploadComponent} from '../../shared/components/avatar-upload/avatar-upload.component'
import {Store} from '@ngrx/store'
import {selectIsUserLoading, selectUser} from '../../user-management/store/user.feature'
import {
  AbstractControl,
  FormControl,
  FormGroup,
  FormGroupDirective,
  NgForm,
  ReactiveFormsModule,
  Validators
} from '@angular/forms'
import {UpdateUser, User} from '../../model/user/user'
import {ButtonComponent} from '../../shared/components/button/button.component'
import {UserValidatorService} from '../../user-management/signup/validators/user-validator.service'

import {hasError} from '../../shared/helper/form-field-error'
import {updateUser, uploadAvatar} from '../../user-management/store/user.actions'
import {passwordMatch} from '../../user-management/signup/validators/password-validator'
import {MatFormFieldModule} from '@angular/material/form-field'
import {MatInput} from '@angular/material/input'
import {MatProgressSpinner} from '@angular/material/progress-spinner'
import {ErrorStateMatcher} from '@angular/material/core'
import {PasswordIconDirective} from '../../shared/directives/password-icon.directive'
import {MatIcon} from '@angular/material/icon'

@Component({
  selector: 'wm-account-settings',
  imports: [
    AvatarUploadComponent,
    ReactiveFormsModule,
    ButtonComponent,
    MatFormFieldModule,
    MatInput,
    MatProgressSpinner,
    PasswordIconDirective,
    MatIcon
  ],
  templateUrl: './account-settings.component.html',
  styleUrl: './account-settings.component.css'
})
export class AccountSettingsComponent {
  private store = inject(Store)
  private userValidatorService = inject(UserValidatorService)

  user: Signal<User | null> = this.store.selectSignal(selectUser)
  isUserLoading: Signal<boolean> = this.store.selectSignal(selectIsUserLoading)

  formGroup = new FormGroup({
    avatar: new FormControl<File | null>(null),
    username: new FormControl<string>(this.user()?.username ?? '', {
      nonNullable: true,
      validators: [Validators.required, Validators.minLength(5)],
      asyncValidators: [this.userValidatorService.usernameAvailable()]
    }),
    email: new FormControl<string>({
      value: this.user()?.email ?? '',
      disabled: true // Email change is disabled for now TODO: enable later
    }, {
      nonNullable: true,
      validators: [Validators.required, Validators.email, this.userValidatorService.emailAvailable],
    })
  })

  pwGroup = new FormGroup({
    currentPassword: new FormControl<string>('', {
      nonNullable: true,
      validators: [Validators.required]
    }),
    changed: new FormGroup({
      password: new FormControl<string>('', {
        nonNullable: true,
        validators: [Validators.required, Validators.minLength(8)]
      }),
      confirmPassword: new FormControl<string>('', {
        nonNullable: true,
        validators: [Validators.required, Validators.minLength(8)]
      })
    }, {validators: [passwordMatch()]})
  })

  save(): void {
    const user = this.user()
    this.formGroup.markAllAsTouched()
    if (this.formGroup.invalid || !user) return

    const value = this.formGroup.getRawValue()
    if (value.avatar) {
      this.store.dispatch(uploadAvatar({file: value.avatar}))
    }

    const dto: UpdateUser = {
      id: user.id
    }

    if (value.username !== user.username) dto.username = value.username
    if (value.email !== user.email) dto.email = value.email
    this.store.dispatch(updateUser({user: dto}))
  }

  changePassword(): void {
    const user = this.user()
    this.pwGroup.markAllAsTouched()
    if (this.pwGroup.invalid || !user) return

    const value = this.pwGroup.getRawValue()
    this.store.dispatch(updateUser({
      user: {
        id: user.id,
        passwordChange: {
          currentPassword: value.currentPassword,
          password: value.changed.password
        }
      }
    }))
  }

  passwordErrorStateMatcher: ErrorStateMatcher = {
    isErrorState: (control: AbstractControl | null, _: FormGroupDirective | NgForm | null): boolean => {
      return !!control && (hasError(control) || this.showPasswordMatchError('password'))
    }
  }

  confirmPasswordErrorStateMatcher: ErrorStateMatcher = {
    isErrorState: (control: AbstractControl | null, _: FormGroupDirective | NgForm | null): boolean => {
      return !!control && (hasError(control) || this.showPasswordMatchError('confirmPassword'))
    }
  }

  showPasswordMatchError(formField: 'password' | 'confirmPassword'): boolean {
    const changedGroup = this.pwGroup.get('changed')
    if (!changedGroup) return false

    return hasError(changedGroup, 'passwordMatch') && !hasError(changedGroup.get(formField))
  }

  protected readonly hasError = hasError
}
