import {Component, inject} from '@angular/core'
import {UserManagementPanelComponent} from '../user-management-panel/user-management-panel.component'
import {ButtonComponent, FormFieldComponent} from '../../shared/material-api'
import {FormBuilder, ReactiveFormsModule, Validators} from '@angular/forms'
import {NgIf} from '@angular/common'
import {hasError} from '../../shared/helper/form-field-error'
import {Store} from '@ngrx/store'
import {userLogin} from '../store/user.actions'

@Component({
  selector: 'wm-login',
  standalone: true,
  imports: [
    UserManagementPanelComponent,
    FormFieldComponent,
    ButtonComponent,
    ReactiveFormsModule,
    NgIf
  ],
  templateUrl: './login.component.html',
  styleUrl: './login.component.css'
})
export class LoginComponent {
  private fb = inject(FormBuilder)
  private store = inject(Store)

  formGroup = this.fb.group({
    username: this.fb.control<string>('', {
      nonNullable: true,
      validators: Validators.required
    }),
    password: this.fb.control<string>('', {
      nonNullable: true,
      validators: Validators.required
    })
  })

  login(): void {
    this.formGroup.markAllAsTouched()
    if (this.formGroup.invalid) return

    const data = this.formGroup.getRawValue()
    this.store.dispatch(userLogin(data))
  }

  protected readonly hasError = hasError
}
