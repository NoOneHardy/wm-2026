import {Component, inject, Signal} from '@angular/core'
import {UserManagementPanelComponent} from '../user-management-panel/user-management-panel.component'
import {FormBuilder, ReactiveFormsModule, Validators} from '@angular/forms'
import {FormFieldComponent} from '../../shared/components/form-field/form-field.component'
import {ButtonComponent} from '../../shared/components/button/button.component'

import {hasError} from '../../shared/helper/form-field-error'
import {UserValidatorService} from './validators/user-validator.service'
import {passwordMatch} from './validators/password-validator'
import {Store} from '@ngrx/store'
import {createUser} from '../store/user.actions'
import {selectIsUserLoading} from '../store/user.feature'

@Component({
  selector: 'wm-signup',
  imports: [
    UserManagementPanelComponent,
    ReactiveFormsModule,
    FormFieldComponent,
    ButtonComponent
  ],
  templateUrl: './signup.component.html',
  styleUrl: './signup.component.css'
})
export class SignupComponent {
  private fb = inject(FormBuilder)
  private userValidatorService = inject(UserValidatorService)
  private store = inject(Store)

  isLoading: Signal<boolean> = this.store.selectSignal(selectIsUserLoading)
  formGroup = this.fb.group({
    username: this.fb.control<string>('', {
      nonNullable: true,
      validators: [
        Validators.required,
        Validators.minLength(5),
      ],
      asyncValidators: [
        this.userValidatorService.usernameAvailable()
      ]
    }),
    email: this.fb.control<string>('', {
      nonNullable: true,
      validators: [
        Validators.required,
        Validators.email
      ],
      asyncValidators: [
        this.userValidatorService.emailAvailable()
      ]
    }),
    firstname: this.fb.control<string>('', {
      nonNullable: true,
      validators: [
        Validators.required,
        Validators.minLength(2)
      ]
    }),
    lastname: this.fb.control<string>('', {
      nonNullable: true,
      validators: [
        Validators.required,
        Validators.minLength(2)
      ]
    }),
    passwords: this.fb.group({
      password: this.fb.control<string>('', {
        nonNullable: true,
        validators: [
          Validators.required,
          Validators.minLength(8)
        ]
      }),
      confirmPassword: this.fb.control<string>('', {
        nonNullable: true,
        validators: [
          Validators.required,
          Validators.minLength(8)
        ]
      })
    }, {
      validators: [passwordMatch()]
    })
  })

  submit(): void {
    this.formGroup.markAllAsTouched()
    if (this.formGroup.invalid) return

    const value = this.formGroup.getRawValue()
    this.store.dispatch(createUser({
      user: {
        ...value,
        password: value.passwords.password
      }
    }))
  }

  protected readonly hasError = hasError
}
