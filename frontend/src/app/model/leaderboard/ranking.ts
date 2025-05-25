export interface Ranking {
  id: string
  username: string
  points: number
  avatar: string | null
  ranking: number
  previousRanking: number
}
