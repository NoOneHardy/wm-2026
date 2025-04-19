export interface Bet {
  id: string
  gameId: string
  scoreTeamHome: number
  scoreTeamGuest: number
  joker: 1 | 2 | 3
  createdAt: Date
  updatedAt: Date
  deletedAt: Date | null
}
