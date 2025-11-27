import {Pipe, PipeTransform} from '@angular/core'
import {NotificationChannel} from '../../user-management/model/notification-channel'

@Pipe({
  name: 'notificationChannel',
  standalone: true
})
export class NotificationChannelPipe implements PipeTransform {
  transform(type: NotificationChannel): string {
    switch (type) {
      case NotificationChannel.IN_APP:
        return 'In-App-Benachrichtigungen'
      case NotificationChannel.EMAIL:
        return 'Email-Benachrichtigungen'
    }
  }
}
