import {Ranking} from '../leaderboard/ranking'
import {BetGame} from '../game/bet-game'
import {UserSummary} from './user-summary'
import {GlobalStatistics} from './global-statistics'
import {Statistics} from './statistics'

export interface DashboardData {
  leaderboardPreview: (Ranking | null)[]
  userSummary: UserSummary
  stats: Statistics
  globalStats: GlobalStatistics
  openBets: BetGame[]
  recentResults: BetGame[]
}
