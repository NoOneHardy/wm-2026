import {Component, inject, OnInit} from '@angular/core'
import {UserManagementPanelComponent} from '../../user-management/user-management-panel/user-management-panel.component'
import {FormControl, FormGroup, FormGroupDirective, NgForm, ReactiveFormsModule, Validators} from '@angular/forms'
import {hasError} from '../../shared/helper/form-field-error'
import {passwordMatch} from '../../user-management/signup/validators/password-validator'
import {ButtonComponent} from '../../shared/components/button/button.component'
import {Store} from '@ngrx/store'
import {selectIsUserLoading} from '../../user-management/store/user.feature'
import {ActivatedRoute, Router} from '@angular/router'
import {resetPassword} from '../../user-management/store/user.actions'
import {ErrorStateMatcher} from '@angular/material/core'
import {MatInput} from '@angular/material/input'
import {MatFormFieldModule} from '@angular/material/form-field'
import {MatIcon} from '@angular/material/icon'
import {PasswordIconDirective} from '../../shared/directives/password-icon.directive'

@Component({
  selector: 'wm-password-reset',
  imports: [
    UserManagementPanelComponent,
    ReactiveFormsModule,
    ButtonComponent,
    MatInput,
    MatFormFieldModule,
    MatIcon,
    PasswordIconDirective
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

  showPasswordMatchError(formField: 'password' | 'confirmPassword'): boolean {
    return hasError(this.formGroup, 'passwordMatch') && !hasError(this.formGroup.get(formField))
  }

  protected readonly hasError = hasError
}
