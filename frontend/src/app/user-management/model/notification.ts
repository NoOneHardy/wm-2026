import {NotificationType} from './notification-type'

export interface Notification {
  id: string
  title: string
  content: string
  route: string
  type: NotificationType
}
