import {Ranking} from '../leaderboard/ranking'

export interface DashboardData {
  leaderboardPreview: (Ranking | null)[]
}
