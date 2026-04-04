import {Component, inject, Signal} from '@angular/core'
import {AvatarUploadComponent} from '../../shared/components/avatar-upload/avatar-upload.component'
import {Store} from '@ngrx/store'
import {selectIsUserLoading, selectUser} from '../../user-management/store/user.feature'
import {FormControl, FormGroup, ReactiveFormsModule, Validators} from '@angular/forms'
import {UpdateUser, User} from '../../model/user/user'
import {FormFieldComponent} from '../../shared/components/form-field/form-field.component'
import {ButtonComponent} from '../../shared/components/button/button.component'
import {UserValidatorService} from '../../user-management/signup/validators/user-validator.service'

import {hasError} from '../../shared/helper/form-field-error'
import {updateUser, uploadAvatar} from '../../user-management/store/user.actions'
import {passwordMatch} from '../../user-management/signup/validators/password-validator'

@Component({
  selector: 'wm-account-settings',
  imports: [
    AvatarUploadComponent,
    ReactiveFormsModule,
    FormFieldComponent,
    ButtonComponent
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

  showPasswordMatchError(formField: string): boolean {
    const changedGroup = this.pwGroup.get('changed')
    if (!changedGroup) return false

    return hasError(changedGroup, 'passwordMatch') && !hasError(changedGroup.get(formField))
  }

  protected readonly hasError = hasError
}
