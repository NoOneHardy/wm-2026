import {Component, inject, OnInit} from '@angular/core'
import {UserManagementPanelComponent} from '../../user-management/user-management-panel/user-management-panel.component'
import {FormFieldComponent} from '../../shared/components/form-field/form-field.component'
import {FormControl, FormGroup, ReactiveFormsModule, Validators} from '@angular/forms'
import {hasError} from '../../shared/helper/form-field-error'
import {passwordMatch} from '../../user-management/signup/validators/password-validator'
import {ButtonComponent} from '../../shared/components/button/button.component'
import {Store} from '@ngrx/store'
import {selectIsUserLoading} from '../../user-management/store/user.feature'
import {ActivatedRoute, Router} from '@angular/router'
import {resetPassword} from '../../user-management/store/user.actions'

@Component({
  selector: 'wm-password-reset',
  standalone: true,
  imports: [
    UserManagementPanelComponent,
    FormFieldComponent,
    ReactiveFormsModule,
    ButtonComponent
  ],
  templateUrl: './password-reset.component.html',
  styleUrl: './password-reset.component.css'
})
export class PasswordResetComponent implements OnInit {
  private store = inject(Store)
  private route = inject(ActivatedRoute)
  private router = inject(Router)

  protected isLoading = this.store.selectSignal(selectIsUserLoading)
  private code = ''

  formGroup = new FormGroup({
    password: new FormControl<string>('', {
      nonNullable: true,
      validators: [Validators.required, Validators.minLength(8)]
    }),
    confirmPassword: new FormControl<string>('', {
      nonNullable: true,
      validators: [Validators.required]
    })
  }, {
    validators: [passwordMatch()]
  })

  ngOnInit(): void {
    const code = this.route.snapshot.queryParamMap.get('code')
    if (!code) return void this.router.navigateByUrl('/')
    this.code = code
  }

  resetPassword(): void {
    this.formGroup.markAllAsTouched()
    if (this.formGroup.invalid) return

    const newPassword = this.formGroup.controls.password.value
    this.store.dispatch(resetPassword({
      code: this.code,
      newPassword
    }))
  }

  protected readonly hasError = hasError
}
