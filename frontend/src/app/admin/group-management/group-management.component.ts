import {Component, computed, DestroyRef, inject, OnInit, signal} from '@angular/core'
import {FormControl, FormGroup, ReactiveFormsModule, Validators} from '@angular/forms'
import {FormFieldComponent} from '../../shared/components/form-field/form-field.component'
import {ButtonComponent} from '../../shared/components/button/button.component'
import {NgIf} from '@angular/common'
import {hasError} from '../../shared/helper/form-field-error'
import {AdminService} from '../admin.service'
import {SnackbarService} from '../../shared/services/snackbar/snackbar.service'
import {takeUntilDestroyed} from '@angular/core/rxjs-interop'
import {GroupOption} from '../model/group-option'
import {finalize} from 'rxjs'

@Component({
  selector: 'wm-group-management',
  imports: [
    ReactiveFormsModule,
    FormFieldComponent,
    ButtonComponent,
    NgIf
  ],
  templateUrl: './group-management.component.html',
  styleUrl: './group-management.component.css'
})
export class GroupManagementComponent implements OnInit {
  private adminService = inject(AdminService)
  private snackbarService = inject(SnackbarService)
  private destroyRef = inject(DestroyRef)

  isSaving = signal(false)
  existingGroups = signal<GroupOption[]>([])
  createdGroup = signal<GroupOption | null>(null)

  suggestedOrder = computed(() => {
    const order = this.existingGroups().reduce((maxOrder, group) => Math.max(maxOrder, group.order), 0)
    return order + 1
  })

  formGroup = new FormGroup({
    name: new FormControl<string>('', {
      nonNullable: true,
      validators: [Validators.required]
    }),
    order: new FormControl<number>(1, {
      nonNullable: true,
      validators: [Validators.required, Validators.min(0)]
    }),
    isKnockout: new FormControl<boolean>(false, {
      nonNullable: true
    }),
    thumbnail: new FormControl<string>('', {
      nonNullable: true
    })
  })

  ngOnInit(): void {
    this.loadGroups()
  }

  submit(): void {
    this.formGroup.markAllAsTouched()
    if (this.formGroup.invalid) return

    const value = this.formGroup.getRawValue()
    const thumbnail = value.isKnockout && value.thumbnail.trim() ? value.thumbnail.trim() : null

    this.isSaving.set(true)
    this.adminService.createGroup({
      name: value.name.trim(),
      order: value.order,
      isKnockout: value.isKnockout,
      thumbnail
    }).pipe(
      finalize(() => this.isSaving.set(false)),
      takeUntilDestroyed(this.destroyRef)
    ).subscribe(group => {
      const createdGroup: GroupOption = {
        id: group.id,
        name: value.name.trim(),
        order: value.order,
        isKnockout: value.isKnockout,
        thumbnail
      }
      this.createdGroup.set(createdGroup)
      this.existingGroups.update(groups => [...groups, createdGroup].sort((a, b) => a.order - b.order || a.name.localeCompare(b.name)))
      this.snackbarService.addMessage({
        type: 'success',
        message: `${createdGroup.name} wurde angelegt`
      })
      this.formGroup.reset({
        name: '',
        order: this.suggestedOrder(),
        isKnockout: false,
        thumbnail: ''
      })
    })
  }

  private loadGroups(): void {
    this.adminService.getGroupOptions().pipe(
      takeUntilDestroyed(this.destroyRef)
    ).subscribe(groups => {
      const sortedGroups = [...groups].sort((a, b) => a.order - b.order || a.name.localeCompare(b.name))
      this.existingGroups.set(sortedGroups)
      this.formGroup.controls.order.setValue(this.suggestedOrder(), {emitEvent: false})
    })
  }

  protected readonly hasError = hasError
}
