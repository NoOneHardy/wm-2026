import {Component, computed, inject} from '@angular/core'
import {Store} from '@ngrx/store'
import {selectNotifications} from '../../../../../user-management/store/user.feature'
import {MatMenu, MatMenuTrigger} from '@angular/material/menu'
import {RouterLink} from '@angular/router'

@Component({
  selector: 'wm-notifications',
  standalone: true,
  imports: [
    MatMenu,
    MatMenuTrigger,
    RouterLink
  ],
  templateUrl: './notifications.component.html',
  styleUrl: './notifications.component.css'
})
export class NotificationsComponent {
  private store = inject(Store)

  notifications = this.store.selectSignal(selectNotifications)
  hasNotifications = computed(() => this.notifications().length > 0)
}
