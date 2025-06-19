import {User} from '../../model/user/user'
import {Role} from '../../model/user/role'
import {FeatureSlice} from '@ngrx/store'
import * as feature from './user.feature'
import {selectIsAdmin, UserState} from './user.feature'
import {userLoggedIn} from './user.actions'
import {mockUser1} from '../../model/mock/user.mock'

describe('UserFeature', () => {
  let store: FeatureSlice<UserState>
  let initialState: UserState
  let mockUser: User

  beforeEach(() => {
    store = feature.userFeature
    initialState = feature.initialState
    mockUser = {...mockUser1}
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
