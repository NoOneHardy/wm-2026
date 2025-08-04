export interface Notification {
  id: string
  title: string
  content: string
  route: string
  type: NotificationType
}

export enum NotificationType {
  NEW_BET,
  NEW_RESULT,
  APPROVAL,
  REJECTION,
  RANKING_UPDATE
}
