import {Component, inject} from '@angular/core'
import {UserManagementPanelComponent} from '../user-management-panel/user-management-panel.component'
import {ButtonComponent, FormFieldComponent} from '../../shared/material-api'
import {FormBuilder, ReactiveFormsModule, Validators} from '@angular/forms'

@Component({
  selector: 'wm-login',
  standalone: true,
  imports: [
    UserManagementPanelComponent,
    FormFieldComponent,
    ButtonComponent,
    ReactiveFormsModule
  ],
  templateUrl: './login.component.html',
  styleUrl: './login.component.css'
})
export class LoginComponent {
  private fb = inject(FormBuilder)

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

    console.log(this.formGroup.getRawValue())
  }
}
