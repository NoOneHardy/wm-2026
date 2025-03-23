import {Component, inject} from '@angular/core'
import {UserManagementPanelComponent} from '../user-management-panel/user-management-panel.component'
import {FormBuilder, ReactiveFormsModule, Validators} from '@angular/forms'
import {FormFieldComponent} from '../../shared/components/form-field/form-field.component'
import {ButtonComponent} from '../../shared/components/button/button.component'
import {NgIf} from '@angular/common'
import {hasError} from '../../shared/helper/form-field-error'
import {UserValidatorService} from './validators/user-validator.service'

@Component({
  selector: 'wm-signup',
  standalone: true,
  imports: [
    UserManagementPanelComponent,
    ReactiveFormsModule,
    FormFieldComponent,
    ButtonComponent,
    NgIf
  ],
  templateUrl: './signup.component.html',
  styleUrl: './signup.component.css'
})
export class SignupComponent {
  private fb = inject(FormBuilder)
  private userValidatorService = inject(UserValidatorService)

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
    })
  })

  submit(): void {
    this.formGroup.markAllAsTouched()

    const value = this.formGroup.getRawValue()
    console.log(value)
  }

  protected readonly hasError = hasError
}
