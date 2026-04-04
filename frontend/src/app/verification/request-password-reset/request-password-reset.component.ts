import {Component, inject} from '@angular/core'
import {FormControl, FormGroup, ReactiveFormsModule, Validators} from '@angular/forms'
import {Store} from '@ngrx/store'
import {requestPasswordResetLink} from '../../user-management/store/user.actions'
import {UserManagementPanelComponent} from '../../user-management/user-management-panel/user-management-panel.component'
import {ButtonComponent} from '../../shared/components/button/button.component'
import {RouterLink} from '@angular/router'
import {FormFieldComponent} from '../../shared/components/form-field/form-field.component'
import {hasError} from '../../shared/helper/form-field-error'
import {selectIsUserLoading} from '../../user-management/store/user.feature'

enum STATE {
  FORM,
  CONFIRMATION
}

@Component({
  selector: 'wm-request-password-reset',
  imports: [
    UserManagementPanelComponent,
    ButtonComponent,
    RouterLink,
    ReactiveFormsModule,
    FormFieldComponent
  ],
  templateUrl: './request-password-reset.component.html',
  styleUrl: './request-password-reset.component.css'
})
export class RequestPasswordResetComponent {
  private store = inject(Store)

  isLoading = this.store.selectSignal(selectIsUserLoading)
  state: STATE = STATE.FORM
  formGroup = new FormGroup({
    email: new FormControl<string>('', {
      nonNullable: true,
      validators: [Validators.required, Validators.email]
    }),
  })

  submit(): void {
    this.formGroup.markAllAsTouched()
    if (this.formGroup.invalid) return

    const email = this.formGroup.controls.email.value

    this.store.dispatch(requestPasswordResetLink({email}))
    this.state = STATE.CONFIRMATION
  }

  protected readonly STATE = STATE
  protected readonly hasError = hasError
}
