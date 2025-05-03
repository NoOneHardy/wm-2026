import {Team} from '../team/team'
import {Score} from './score'
import {Bet} from './bet'

export interface BetGame {
  id: string
  timestamp: Date
  teamHome: Team
  teamGuest: Team
  result: Score | null
  bet: Bet | null
}
