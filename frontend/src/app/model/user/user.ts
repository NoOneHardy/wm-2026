import {Role} from './role'
import {UserApplicationStatus} from './user-application-status'

export interface User {
  id: string
  username: string
  email: string
  firstname: string
  lastname: string
  points: number
  lastReviewedPoints: number
  avatarUrl?: string | null
  role: Role
  applicationReviewedAt?: Date | null
  userApplicationStatus: UserApplicationStatus
}

export interface NewUser {
  username: string
  email: string
  firstname: string
  lastname: string
  password: string
}

export interface UpdateUser {
  id: string
  username?: string
  email?: string
}
