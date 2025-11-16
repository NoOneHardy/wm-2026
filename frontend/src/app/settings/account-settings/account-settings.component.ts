import {Component, inject, Signal} from '@angular/core'
import {AvatarUploadComponent} from '../../shared/components/avatar-upload/avatar-upload.component'
import {Store} from '@ngrx/store'
import {selectIsUserLoading, selectUser} from '../../user-management/store/user.feature'
import {FormControl, FormGroup, ReactiveFormsModule, Validators} from '@angular/forms'
import {User} from '../../model/user/user'
import {FormFieldComponent} from '../../shared/components/form-field/form-field.component'
import {ButtonComponent} from '../../shared/components/button/button.component'
import {UserValidatorService} from '../../user-management/signup/validators/user-validator.service'
import {NgIf} from '@angular/common'
import {hasError} from '../../shared/helper/form-field-error'
import {uploadAvatar} from '../../user-management/store/user.actions'

@Component({
  selector: 'wm-account-settings',
  standalone: true,
  imports: [
    AvatarUploadComponent,
    ReactiveFormsModule,
    FormFieldComponent,
    ButtonComponent,
    NgIf
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
    email: new FormControl<string>(this.user()?.email ?? '', {
      nonNullable: true,
      validators: [Validators.required, Validators.email, this.userValidatorService.emailAvailable],
    })
  })

  save(): void {
    this.formGroup.markAllAsTouched()
    if (this.formGroup.invalid) return

    const value = this.formGroup.getRawValue()
    if (value.avatar) {
      this.store.dispatch(uploadAvatar({file: value.avatar}))
    }
  }

  protected readonly hasError = hasError
}
