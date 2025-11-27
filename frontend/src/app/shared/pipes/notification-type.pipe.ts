import {Pipe, PipeTransform} from '@angular/core'
import {NotificationType} from '../../user-management/model/notification-type'

@Pipe({
  name: 'notificationType',
  standalone: true
})
export class NotificationTypePipe implements PipeTransform {
  transform(type: NotificationType): string {
    switch (type) {
      case NotificationType.NEW_GAME:
        return 'Neues Spiel hinzugefügt'
      case NotificationType.NEW_RESULT:
        return 'Neues Resultat verfügbar'
      case NotificationType.APPROVAL:
        return 'Account wurde genehmigt'
      case NotificationType.REJECTION:
        return 'Account wurde abgelehnt'
      case NotificationType.RANKING_UPDATE:
        return 'Ranglisten-Update'
    }
  }
}
