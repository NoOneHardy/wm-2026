import {BetGame} from '../game/bet-game'

export interface Group {
  id: string
  name: string
  percentage: number
  percentageResult: number
  lastSavedAt: Date
  lastSavedAtResult: Date
  games: BetGame[]
  isKnockout: boolean
  createdAt: Date
  updatedAt: Date
  deletedAt: Date | null
}
