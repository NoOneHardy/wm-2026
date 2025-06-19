import {User} from '../../model/user/user'
import {Role} from '../../model/user/role'
import {FeatureSlice} from '@ngrx/store'
import * as feature from './user.feature'
import {selectIsAdmin, UserState} from './user.feature'
import {userLoggedIn} from './user.actions'
import {UserApplicationStatus} from '../../model/user/user-application-status'

const mockUser: User = {
  id: 'user-1',
  username: 'User',
  email: 'user@no1hardy.ch',
  firstname: 'Silas',
  lastname: 'No1hardy',
  role: Role.USER,
  points: 0,
  lastReviewedPoints: 0,
  userApplicationStatus: UserApplicationStatus.PENDING
}

describe('UserFeature', () => {
  let store: FeatureSlice<UserState>
  let initialState: UserState

  beforeEach(() => {
    store = feature.userFeature
    initialState = feature.initialState
  })

  it('should initialize', () => {
    expect(store).toBeTruthy()
  })

  it('should return whether a user is admin', () => {
    let state = initialState
    expect(selectIsAdmin.projector(state.user)).toBeFalse()
    state = store.reducer(state, userLoggedIn({
      ...mockUser,
      role: Role.ADMIN
    }))
    expect(selectIsAdmin.projector(state.user)).toBeTrue()
    state = store.reducer(state, userLoggedIn(mockUser))
    expect(selectIsAdmin.projector(state.user)).toBeFalse()
  })
})
