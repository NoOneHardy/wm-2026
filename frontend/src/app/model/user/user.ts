import {Role} from './role'
import {UserApplicationStatus} from './user-application-status'

export interface User {
  id: string
  username: string
  email: string
  firstname: string
  lastname: string
  createdAt: Date
  isActive: boolean
  points: number
  updatedAt: Date
  role: Role
  lastReviewedPoints?: number | null
  avatar?: string | null
  applicationReviewedAt?: Date | null
  userApplicationStatus?: UserApplicationStatus | null
}

export interface NewUser {
  username: string
  email: string
  firstname: string
  lastname: string
  password: string
}
