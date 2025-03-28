import {Component, inject, input, signal} from '@angular/core'
import {User} from '../../../../../model/user/user'
import {RouterLink} from '@angular/router'
import {NgIf} from '@angular/common'
import {UserButtonComponent} from '../user-button/user-button.component'
import {Store} from '@ngrx/store'
import {logout} from '../../../../../user-management/store/user.actions'

@Component({
  selector: 'wm-user-menu',
  standalone: true,
  imports: [
    RouterLink,
    NgIf,
    UserButtonComponent
  ],
  templateUrl: './user-menu.component.html',
  styleUrl: './user-menu.component.css'
})
export class UserMenuComponent {
  private store = inject(Store)
  user = input<User | null>()
  hasNotifications = false

  isDropdownExpanded = signal(false)

  toggleDropdown(): void {
    this.isDropdownExpanded.update(isExpanded => !isExpanded)
  }

  logout(): void {
    this.store.dispatch(logout())
  }
}
