import {User} from '../../model/user/user'
import {Role} from '../../model/user/role'
import {FeatureSlice} from '@ngrx/store'
import * as feature from './user.feature'
import {selectIsAdmin, UserState} from './user.feature'
import {
  createUserFailed,
  markedNotificationAsRead,
  markNotificationAsRead,
  passwordResetDone,
  passwordResetLinkRequested,
  requestPasswordResetLink,
  resetPassword,
  updateNotifications,
  userLoggedIn
} from './user.actions'
import {mockUser1} from '../../model/mock/user.mock'
import {NotificationType} from '../model/notification-type'

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

  it('should set isUserLoading to true on markNotificationAsRead', () => {
    let state = initialState
    expect(state.isUserLoading).toBeFalse()
    state = store.reducer(state, markNotificationAsRead({id: 'asdf'}))
    expect(state.isUserLoading).toBeTrue()
  })

  it('should set isUserLoading to false on markedNotificationAsRead', () => {
    let state = initialState
    expect(state.isUserLoading).toBeFalse()
    state = store.reducer(state, markNotificationAsRead({id: 'asdf'}))
    expect(state.isUserLoading).toBeTrue()
    state = store.reducer(state, markedNotificationAsRead())
    expect(state.isUserLoading).toBeFalse()
  })

  it('should update the user notifications', () => {
    let state = initialState
    expect(state.notifications).toEqual([])
    state = store.reducer(state, updateNotifications({
      notifications: [{
        id: 'not-1',
        title: 'WOW',
        content: 'IT\'S PIKATCHU!!',
        route: '/exit',
        type: NotificationType.APPROVAL
      }]
    }))

    expect(state.notifications).toHaveSize(1)
    expect(state.notifications).toEqual([{
      id: 'not-1',
      title: 'WOW',
      content: 'IT\'S PIKATCHU!!',
      route: '/exit',
      type: NotificationType.APPROVAL
    }])
  })

  it('should set isUserLoading to true on requestPasswordResetLink', () => {
    let state = initialState
    expect(state.isUserLoading).toBeFalse()
    state = store.reducer(state, requestPasswordResetLink({email: 'silas@test.ch'}))

    expect(state.isUserLoading).toBeTrue()
  })

  it('should set isUserLoading to false on requestedPasswordResetLink', () => {
    let state: UserState = {
      ...initialState,
      isUserLoading: true
    }
    expect(state.isUserLoading).toBeTrue()
    state = store.reducer(state, passwordResetLinkRequested({isSent: true}))
    expect(state.isUserLoading).toBeFalse()
  })

  it('should set isUserLoading to true on resetPassword', () => {
    let state = initialState
    expect(state.isUserLoading).toBeFalse()
    state = store.reducer(state, resetPassword({code: '123', newPassword: '123'}))

    expect(state.isUserLoading).toBeTrue()
  })

  it('should set isUserLoading to false on passwordResetDone', () => {
    let state: UserState = {
      ...initialState,
      isUserLoading: true
    }
    expect(state.isUserLoading).toBeTrue()
    state = store.reducer(state, passwordResetDone({success: true}))
    expect(state.isUserLoading).toBeFalse()
  })

  it('should set isUserLoading to false on createUserFailed', () => {
    let state: UserState = {
      ...initialState,
      isUserLoading: true
    }
    expect(state.isUserLoading).toBeTrue()
    state = store.reducer(state, createUserFailed())
    expect(state.isUserLoading).toBeFalse()
  })
})
