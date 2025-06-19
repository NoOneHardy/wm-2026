import {Component, computed, inject, OnInit, Signal} from '@angular/core'
import {Store} from '@ngrx/store'
import {selectUsers} from '../store/admin.feature'
import {NgForOf, NgOptimizedImage} from '@angular/common'
import {confirmUser, denyUser, loadUsers} from '../store/admin.actions'
import {User} from '../../model/user/user'
import {UserApplicationStatus} from '../../model/user/user-application-status'
import {selectUser} from '../../user-management/store/user.feature'
import {MatRipple} from '@angular/material/core'

@Component({
  selector: 'wm-user-management',
  standalone: true,
  imports: [
    NgForOf,
    NgOptimizedImage,
    MatRipple
  ],
  templateUrl: './user-management.component.html',
  styleUrl: './user-management.component.css'
})
export class UserManagementComponent implements OnInit {
  private store = inject(Store)

  _users: Signal<User[]> = this.store.selectSignal(selectUsers)
  currentUser: Signal<User | null> = this.store.selectSignal(selectUser)

  get users(): Signal<User[]> {
    return computed(() => {
      return this._users().filter(user => user.id !== this.currentUser()?.id).sort((a, b) => {
        if (a.userApplicationStatus === b.userApplicationStatus) return a.username.localeCompare(b.username)

        if (this.isDenied(a) && this.isAccepted(b)) return -1
        if (this.isAccepted(a) && this.isDenied(b)) return 1

        if (this.isAccepted(a) || this.isDenied(a)) return 1
        return -1
      })
    })
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
}
