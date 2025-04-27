import {BetGame} from '../game/bet-game'
import {AvailableJokers} from './available-jokers'

export interface Group {
  id: string
  name: string
  percentage: number
  percentageResult: number
  lastSavedAt: Date
  lastSavedAtResult: Date
  games: BetGame[]
  isKnockout: boolean
  availableJokers: AvailableJokers
  createdAt: Date
  updatedAt: Date
  deletedAt: Date | null
}
