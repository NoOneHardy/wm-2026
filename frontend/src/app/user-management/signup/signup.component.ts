import {Component, inject} from '@angular/core'
import {UserManagementPanelComponent} from '../user-management-panel/user-management-panel.component'
import {FormBuilder, ReactiveFormsModule} from '@angular/forms'
import {FormFieldComponent} from '../../shared/components/form-field/form-field.component'
import {ButtonComponent} from '../../shared/components/button/button.component'

@Component({
  selector: 'wm-signup',
  standalone: true,
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

  formGroup = this.fb.group({
    username: this.fb.control<string>(''),
    email: this.fb.control<string>(''),
    firstname: this.fb.control<string>(''),
    lastname: this.fb.control<string>(''),
    password: this.fb.group({
      password: this.fb.control<string>(''),
      confirmPassword: this.fb.control<string>('')
    })
  })
}
