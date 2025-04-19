export interface Score {
  id: string
  gameId: string
  scoreTeamHome: number
  scoreTeamGuest: number
  createdAt: Date
  updatedAt: Date
  deletedAt: Date | null
}
