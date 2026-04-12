import {Component, inject, OnInit} from '@angular/core'
import {UserManagementPanelComponent} from '../user-management-panel/user-management-panel.component'
import {ButtonComponent} from '../../shared/material-api'
import {FormBuilder, ReactiveFormsModule, Validators} from '@angular/forms'
import {hasError} from '../../shared/helper/form-field-error'
import {Store} from '@ngrx/store'
import {resetError, userLogin} from '../store/user.actions'
import {selectError} from '../store/user.feature'
import {RouterLink} from '@angular/router'
import {MatInput, MatSuffix} from '@angular/material/input'
import {MatIcon} from '@angular/material/icon'
import {PasswordIconDirective} from '../../shared/directives/password-icon.directive'
import {MatFormFieldModule} from '@angular/material/form-field'

@Component({
  selector: 'wm-login',
  imports: [
    UserManagementPanelComponent,
    ButtonComponent,
    ReactiveFormsModule,
    RouterLink,
    MatFormFieldModule,
    MatInput,
    MatSuffix,
    MatIcon,
    PasswordIconDirective
  ],
  templateUrl: './login.component.html',
  styleUrl: './login.component.css'
})
export class LoginComponent implements OnInit {
  private fb = inject(FormBuilder)
  private store = inject(Store)

  userError = this.store.selectSignal(selectError)

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

  ngOnInit(): void {
    this.formGroup.valueChanges.subscribe(() => {
      this.store.dispatch(resetError())
    })
  }

  login(): void {
    this.formGroup.markAllAsTouched()
    if (this.formGroup.invalid) return

    const data = this.formGroup.getRawValue()
    this.store.dispatch(userLogin(data))
  }

  protected readonly hasError = hasError
}
