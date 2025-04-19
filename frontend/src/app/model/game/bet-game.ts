import {Team} from '../team/team'
import {Score} from './score'
import {Bet} from './bet'

export interface BetGame {
  id: string
  timestamp: Date
  teamHome: Team
  teamGuest: Team
  result: Score
  bet: Bet
  createdAt: Date
  updatedAt: Date
  deletedAt: Date | null
}
