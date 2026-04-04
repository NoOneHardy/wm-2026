import {Component, ElementRef, inject, input, signal} from '@angular/core'
import {User} from '../../../../../model/user/user'
import {RouterLink} from '@angular/router'
import {NgIf} from '@angular/common'
import {UserButtonComponent} from '../user-button/user-button.component'
import {Store} from '@ngrx/store'
import {logout} from '../../../../../user-management/store/user.actions'
import {NotificationsComponent} from '../notifications/notifications.component'

@Component({
  selector: 'wm-user-menu',
  host: {
    '(document:click)': 'closeDropdownOnBlur($event)',
  },
  imports: [
    RouterLink,
    NgIf,
    UserButtonComponent,
    NotificationsComponent
  ],
  templateUrl: './user-menu.component.html',
  styleUrl: './user-menu.component.css'
})
export class UserMenuComponent {
  private store = inject(Store)
  private el = inject(ElementRef)

  user = input<User | null>()

  isDropdownExpanded = signal(false)

  toggleDropdown(): void {
    this.isDropdownExpanded.update(isExpanded => !isExpanded)
  }

  closeDropdownOnBlur(e: MouseEvent): void {
    if (!this.el.nativeElement.contains(e.target)) {
      this.isDropdownExpanded.set(false)
    }
  }

  logout(): void {
    this.store.dispatch(logout())
  }
}
