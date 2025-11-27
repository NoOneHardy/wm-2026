import {NotificationType} from './notification-type'
import {NotificationChannel} from './notification-channel'

export interface NotificationPreference {
  type: NotificationType
  channel: NotificationChannel
  selected: boolean
}
