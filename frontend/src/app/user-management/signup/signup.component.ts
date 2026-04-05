import {Component, inject, Signal} from '@angular/core'
import {UserManagementPanelComponent} from '../user-management-panel/user-management-panel.component'
import {FormBuilder, FormControl, FormGroupDirective, NgForm, ReactiveFormsModule, Validators} from '@angular/forms'
import {ButtonComponent} from '../../shared/components/button/button.component'

import {hasError} from '../../shared/helper/form-field-error'
import {UserValidatorService} from './validators/user-validator.service'
import {passwordMatch} from './validators/password-validator'
import {Store} from '@ngrx/store'
import {createUser} from '../store/user.actions'
import {selectIsUserLoading} from '../store/user.feature'
import {MatFormFieldModule} from '@angular/material/form-field'
import {ErrorStateMatcher} from '@angular/material/core'
import {MatInput} from '@angular/material/input'
import {MatProgressSpinner} from '@angular/material/progress-spinner'
import {MatIcon} from '@angular/material/icon'
import {PasswordIconDirective} from '../../shared/directives/password-icon.directive'

@Component({
  selector: 'wm-signup',
  imports: [
    UserManagementPanelComponent,
    ReactiveFormsModule,
    ButtonComponent,
    MatFormFieldModule,
    MatInput,
    MatProgressSpinner,
    MatIcon,
    PasswordIconDirective
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

  passwordErrorStateMatcher: ErrorStateMatcher = {
    isErrorState: (control: FormControl | null, _: FormGroupDirective | NgForm | null): boolean => {
      return !!control && (hasError(control) || this.showPasswordMatchError('password'))
    }
  }

  confirmPasswordErrorStateMatcher: ErrorStateMatcher = {
    isErrorState: (control: FormControl | null, _: FormGroupDirective | NgForm | null): boolean => {
      return !!control && (hasError(control) || this.showPasswordMatchError('confirmPassword'))
    }
  }

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

  showPasswordMatchError(formField: 'password' | 'confirmPassword'): boolean {
    const passwordGroup = this.formGroup.get('passwords')
    if (!passwordGroup) return false

    return hasError(passwordGroup, 'passwordMatch') && !hasError(passwordGroup.get(formField))
  }

  protected readonly hasError = hasError
}
