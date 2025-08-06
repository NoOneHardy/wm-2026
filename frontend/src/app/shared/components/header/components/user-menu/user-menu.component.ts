import {Component, computed, ElementRef, inject, input, signal} from '@angular/core'
import {User} from '../../../../../model/user/user'
import {RouterLink} from '@angular/router'
import {NgIf} from '@angular/common'
import {UserButtonComponent} from '../user-button/user-button.component'
import {Store} from '@ngrx/store'
import {logout} from '../../../../../user-management/store/user.actions'
import {Notification} from '../../../../../user-management/model/notification'
import {NotificationsComponent} from '../notifications/notifications.component'

@Component({
  selector: 'wm-user-menu',
  standalone: true,
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
  notifications = input<Notification[]>([])
  hasNotifications = computed(() => {
    const notifications = this.notifications()
    return notifications && notifications.length > 0
  })

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
