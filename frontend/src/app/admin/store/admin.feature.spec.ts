import {FeatureSlice} from '@ngrx/store'
import * as feature from './admin.feature'
import {AdminState} from './admin.feature'
import {User} from '../../model/user/user'
import {mockUser1} from '../../model/mock/user.mock'
import {applicationReviewed, confirmUser, denyUser, loadUsers, usersLoaded} from './admin.actions'
import {UserApplicationStatus} from '../../model/user/user-application-status'

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

  it('should set isAdminLoading to true when confirming a user', () => {
    let state = initialState
    state = store.reducer(state, confirmUser({id: 'user-id'}))
    expect(state.isAdminLoading).toBeTrue()
  })

  it('should update users when user has been confirmed or denied', () => {
    let state = initialState
    const user1Date = mockUser.applicationReviewedAt
    state = store.reducer(state, usersLoaded({users: [mockUser, {...mockUser, id: 'user-2'}]}))
    expect(state.users).toHaveSize(2)
    state = store.reducer(state, applicationReviewed({
      user: {
        ...mockUser,
        id: 'user-2',
        userApplicationStatus: UserApplicationStatus.ACCEPTED,
        applicationReviewedAt: new Date('2025-06-20T10:56:00')
      }
    }))
    expect(state.users).toHaveSize(2)

    const user1 = state.users.find(user => user.id === 'user-1')
    const user2 = state.users.find(user => user.id === 'user-2')
    expect(user2?.userApplicationStatus).toBe(UserApplicationStatus.ACCEPTED)
    expect(user2?.applicationReviewedAt).toEqual(new Date('2025-06-20T10:56:00'))
    expect(user1?.applicationReviewedAt).toEqual(user1Date)
    expect(state.isAdminLoading).toBeFalse()
  })

  it('should set isAdminLoading to true when denying a user', () => {
    let state = initialState
    state = store.reducer(state, denyUser({id: 'user-id'}))
    expect(state.isAdminLoading).toBeTrue()
  })
})
