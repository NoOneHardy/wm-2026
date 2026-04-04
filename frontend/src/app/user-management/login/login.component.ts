import {Component, inject, OnInit} from '@angular/core'
import {UserManagementPanelComponent} from '../user-management-panel/user-management-panel.component'
import {ButtonComponent, FormFieldComponent} from '../../shared/material-api'
import {FormBuilder, ReactiveFormsModule, Validators} from '@angular/forms'

import {hasError} from '../../shared/helper/form-field-error'
import {Store} from '@ngrx/store'
import {resetError, userLogin} from '../store/user.actions'
import {selectError} from '../store/user.feature'
import {RouterLink} from '@angular/router'

@Component({
  selector: 'wm-login',
  imports: [
    UserManagementPanelComponent,
    FormFieldComponent,
    ButtonComponent,
    ReactiveFormsModule,
    RouterLink
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
