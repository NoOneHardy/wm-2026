import {Component, inject, OnInit, Signal} from '@angular/core'
import {MatSlideToggle} from '@angular/material/slide-toggle'
import {Store} from '@ngrx/store'
import {fetchNotificationPreferences} from '../../user-management/store/user.actions'
import {NotificationPreference} from '../../user-management/model/notification-preference'
import {selectNotificationPreferences} from '../../user-management/store/user.feature'
import {NotificationTypePipePipe} from '../../shared/pipes/notification-type.pipe'

@Component({
  selector: 'wm-notification-settings',
  standalone: true,
  imports: [
    MatSlideToggle,
    NotificationTypePipePipe
  ],
  templateUrl: './notification-settings.component.html',
  styleUrl: './notification-settings.component.css'
})
export class NotificationSettingsComponent implements OnInit {
  private store = inject(Store)

  notifications: Signal<NotificationPreference[]> = this.store.selectSignal(selectNotificationPreferences)

  ngOnInit(): void {
    this.store.dispatch(fetchNotificationPreferences())
  }
}
