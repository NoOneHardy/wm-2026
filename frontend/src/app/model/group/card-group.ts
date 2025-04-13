export interface CardGroup {
  id: string
  name: string
  percentage: number
  percentageResult: number
  thumbnail: string[]
  order: number
  isKnockout: boolean
  createdAt: Date
  updatedAt: Date
  deletedAt: Date | null
}
