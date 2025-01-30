import {Component, input, signal} from '@angular/core'
import {User} from '../../../model/user/user'
import {RouterLink} from '@angular/router'
import {NgIf} from '@angular/common'
import {UserButtonComponent} from '../user-button/user-button.component'

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
  user = input<User | null>()
  hasNotifications = false

  isDropdownExpanded = signal(false)

  toggleDropdown(): void {
    this.isDropdownExpanded.update(isExpanded => !isExpanded)
  }

  logout(): void {
    //
  }
}
