export interface Notification {
  id: string
  title: string
  content: string
  route: string
  type: NotificationType
}

export enum NotificationType {
  NEW_BET = 'NEW_BET',
  NEW_RESULT = 'NEW_RESULT',
  APPROVAL = 'APPROVAL',
  REJECTION = 'REJECTION',
  RANKING_UPDATE = 'RANKING_UPDATE'
}
