import {Component, computed, inject, input} from '@angular/core'
import {Store} from '@ngrx/store'
import {selectNotifications} from '../../../../../user-management/store/user.feature'
import {MatMenu, MatMenuTrigger} from '@angular/material/menu'
import {RouterLink} from '@angular/router'
import {MatRipple} from '@angular/material/core'
import {markNotificationAsRead} from '../../../../../user-management/store/user.actions'

@Component({
  selector: 'wm-notifications',
  standalone: true,
  imports: [
    MatMenu,
    MatMenuTrigger,
    RouterLink,
    MatRipple
  ],
  templateUrl: './notifications.component.html',
  styleUrl: './notifications.component.css'
})
export class NotificationsComponent {
  private store = inject(Store)

  notifications = this.store.selectSignal(selectNotifications)
  hasNotifications = computed(() => this.notifications().length > 0)

  mobile = input<boolean, boolean | ''>(false, {
    transform: v => v === '' || v
  })

  markAsRead(id: string): void {
    this.store.dispatch(markNotificationAsRead({id}))
  }
}
