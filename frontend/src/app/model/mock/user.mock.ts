import {User} from '../user/user'
import {UserApplicationStatus} from '../user/user-application-status'
import {Role} from '../user/role'

export const mockUser1: User = Object.freeze({
  id: 'user-1',
  username: 'User1',
  email: 'user1@no1hardy.ch',
  firstname: 'Max',
  lastname: 'Muster',
  points: 100,
  lastReviewedPoints: 50,
  role: Role.USER,
  userApplicationStatus: UserApplicationStatus.ACCEPTED,
  applicationReviewedAt: new Date('2025-06-19T15:34:00')
})
