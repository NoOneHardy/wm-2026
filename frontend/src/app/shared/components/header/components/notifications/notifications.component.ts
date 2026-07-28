import {Component, computed, inject, input, ChangeDetectionStrategy} from '@angular/core'
import {Store} from '@ngrx/store'
import {selectNotifications} from '../../../../../user-management/store/user.feature'
import {MatMenu, MatMenuTrigger} from '@angular/material/menu'
import {RouterLink} from '@angular/router'
import {MatRipple} from '@angular/material/core'
import {markNotificationAsRead} from '../../../../../user-management/store/user.actions'
import {NotificationType} from '../../../../../user-management/model/notification-type'

@Component({
  selector: 'bet-notifications',
  imports: [
    MatMenu,
    MatMenuTrigger,
    RouterLink,
    MatRipple
  ],
  templateUrl: './notifications.component.html',
  changeDetection: ChangeDetectionStrategy.Eager,
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

  getIcon(type: NotificationType): string {
    switch (type) {
    case NotificationType.NEW_GAME:
      return 'ballot'
    case NotificationType.NEW_RESULT:
      return 'scoreboard'
    case NotificationType.APPROVAL:
      return 'check_circle'
    case NotificationType.REJECTION:
      return 'do_not_disturb_on'
    case NotificationType.RANKING_UPDATE:
      return 'bookmark_star'
    }
  }
}
