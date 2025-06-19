import {FeatureSlice} from '@ngrx/store'
import {AdminState} from './admin.feature'
import {User} from '../../model/user/user'
import * as feature from './admin.feature'
import {mockUser1} from '../../model/mock/user.mock'
import {loadUsers, usersLoaded} from './admin.actions'

describe('AdminFeature', () => {
  let store: FeatureSlice<AdminState>
  let initialState: AdminState
  let mockUser: User

  beforeEach(() => {
    store = feature.adminFeature
    initialState = feature.initialState
    mockUser = {...mockUser1}
  })

  it('should initialize', () => {
    expect(store).toBeTruthy()
  })

  it('should set isAdminLoading to true', () => {
    let state = initialState
    expect(state.isAdminLoading).toBeFalse()
    state = store.reducer(state, loadUsers())
    expect(state.isAdminLoading).toBeTrue()
  })

  it('should set users when user have been loaded', () => {
    let state = initialState
    expect(state.users).toEqual([])
    state = store.reducer(state, usersLoaded({users: [mockUser]}))
    expect(state.users).toEqual([mockUser])
  })

  it('should set isAdminLoading to false when user have been loaded', () => {
    let state = initialState
    state = store.reducer(state, loadUsers())
    expect(state.isAdminLoading).toBeTrue()
    state = store.reducer(state, usersLoaded({users: [mockUser]}))
    expect(state.isAdminLoading).toBeFalse()
  })
})
