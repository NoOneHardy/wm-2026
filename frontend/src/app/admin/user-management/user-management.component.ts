import {Component, computed, inject, OnInit, Signal} from '@angular/core'
import {Store} from '@ngrx/store'
import {selectIsAdminLoading, selectUsers} from '../store/admin.feature'
import {NgOptimizedImage} from '@angular/common'
import {confirmUser, denyUser, loadUsers} from '../store/admin.actions'
import {User} from '../../model/user/user'
import {UserApplicationStatus} from '../../model/user/user-application-status'
import {selectUser} from '../../user-management/store/user.feature'
import {MatRipple} from '@angular/material/core'
import {SpinnerComponent} from '../../shared/components/spinner/spinner.component'
import {MatButtonToggle, MatButtonToggleGroup} from '@angular/material/button-toggle'
import {MatIcon} from '@angular/material/icon'
import {FormControl, FormGroup, ReactiveFormsModule} from '@angular/forms'
import {toSignal} from '@angular/core/rxjs-interop'
import {MatFormField, MatInput, MatLabel, MatPrefix} from '@angular/material/input'

@Component({
  selector: 'bet-user-management',
  imports: [
    NgOptimizedImage,
    MatRipple,
    SpinnerComponent,
    MatButtonToggleGroup,
    MatButtonToggle,
    MatIcon,
    ReactiveFormsModule,
    MatFormField,
    MatInput,
    MatLabel,
    MatPrefix
  ],
  templateUrl: './user-management.component.html',
  styleUrl: './user-management.component.css'
})
export class UserManagementComponent implements OnInit {
  private store = inject(Store)

  _users: Signal<User[]> = this.store.selectSignal(selectUsers)
  currentUser: Signal<User | null> = this.store.selectSignal(selectUser)
  isLoading: Signal<boolean> = this.store.selectSignal(selectIsAdminLoading)

  formGroup = new FormGroup({
    quickFilter: new FormControl<UserApplicationStatus[]>([UserApplicationStatus.PENDING], {nonNullable: true}),
    search: new FormControl<string>('', {nonNullable: true})
  })

  filters = toSignal(this.formGroup.valueChanges)

  users = computed(() => {
    const filters = this.filters()
    const quickFilter = filters?.quickFilter ?? this.formGroup.controls.quickFilter.getRawValue()
    const search = filters?.search ?? this.formGroup.controls.search.getRawValue()

    return this._users()
      .filter(user => user.id !== this.currentUser()?.id)
      .filter(user => quickFilter.length === 0 || quickFilter.includes(user.userApplicationStatus))
      .filter(user => !search || this.checkUser(user, search))
      .sort((a, b) => {
        if (a.userApplicationStatus === b.userApplicationStatus) return a.username.localeCompare(b.username)

        if (this.isDenied(a) && this.isAccepted(b)) return -1
        if (this.isAccepted(a) && this.isDenied(b)) return 1

        if (this.isAccepted(a) || this.isDenied(a)) return 1
        return -1
      })
  })

  private checkUser(user: User, search: string): boolean {
    const normalizedSearch: string = search.toLowerCase().trim()

    return user.username.toLowerCase().includes(normalizedSearch)
      || user.email.toLowerCase().includes(normalizedSearch)
      || user.firstname.toLowerCase().includes(normalizedSearch)
      || user.lastname.toLowerCase().includes(normalizedSearch)
  }

  ngOnInit(): void {
    this.store.dispatch(loadUsers())
  }

  isAccepted(user: User): boolean {
    return user.userApplicationStatus === UserApplicationStatus.ACCEPTED && !!user.applicationReviewedAt
  }

  isDenied(user: User): boolean {
    return user.userApplicationStatus === UserApplicationStatus.DENIED && !!user.applicationReviewedAt
  }

  accept(id: string): void {
    this.store.dispatch(confirmUser({id}))
  }

  deny(id: string): void {
    this.store.dispatch(denyUser({id}))
  }

  protected readonly UserApplicationStatus = UserApplicationStatus
}
