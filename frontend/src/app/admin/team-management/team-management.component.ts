import {Component, DestroyRef, inject, signal} from '@angular/core'
import {FormControl, FormGroup, ReactiveFormsModule, Validators} from '@angular/forms'
import {AvatarUploadComponent} from '../../shared/components/avatar-upload/avatar-upload.component'
import {FormFieldComponent} from '../../shared/components/form-field/form-field.component'
import {ButtonComponent} from '../../shared/components/button/button.component'
import {NgIf, NgOptimizedImage} from '@angular/common'
import {hasError} from '../../shared/helper/form-field-error'
import {AdminService} from '../admin.service'
import {SnackbarService} from '../../shared/services/snackbar/snackbar.service'
import {finalize, switchMap} from 'rxjs'
import {takeUntilDestroyed} from '@angular/core/rxjs-interop'
import {LightTeam} from '../../model/team/light-team'

@Component({
  selector: 'wm-team-management',
  standalone: true,
  imports: [
    ReactiveFormsModule,
    AvatarUploadComponent,
    FormFieldComponent,
    ButtonComponent,
    NgIf,
    NgOptimizedImage
  ],
  templateUrl: './team-management.component.html',
  styleUrl: './team-management.component.css'
})
export class TeamManagementComponent {
  private adminService = inject(AdminService)
  private snackbarService = inject(SnackbarService)
  private destroyRef = inject(DestroyRef)

  isSaving = signal(false)
  createdTeam = signal<LightTeam | null>(null)

  formGroup = new FormGroup({
    flag: new FormControl<File | null>(null, {
      validators: [Validators.required]
    }),
    name: new FormControl<string>('', {
      nonNullable: true,
      validators: [Validators.required]
    }),
    shortName: new FormControl<string>('', {
      nonNullable: true,
      validators: [Validators.required, Validators.minLength(2), Validators.maxLength(4)]
    })
  })

  submit(): void {
    this.formGroup.markAllAsTouched()
    if (this.formGroup.invalid) return

    const value = this.formGroup.getRawValue()
    if (!value.flag) return

    this.isSaving.set(true)
    this.adminService.uploadFlag(value.flag).pipe(
      switchMap(flag => this.adminService.createTeam({
        name: value.name.trim(),
        shortName: value.shortName.trim().toUpperCase(),
        flag
      })),
      finalize(() => this.isSaving.set(false)),
      takeUntilDestroyed(this.destroyRef)
    ).subscribe(team => {
      this.createdTeam.set(team)
      this.snackbarService.addMessage({
        type: 'success',
        message: `${team.name} wurde angelegt`
      })
      this.formGroup.reset({
        flag: null,
        name: '',
        shortName: ''
      })
    })
  }

  protected readonly hasError = hasError
}
