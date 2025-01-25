export interface User {
  id: number
  username: string
  email: string
  firstname: string
  lastname: string
  createdAt: Date
  isActive: boolean
  points: number
  updatedAt: Date
  lastReviewedRank: number
  avatar: string
  confirmedAt: Date
}
