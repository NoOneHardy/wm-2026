import {User} from '../../model/user/user'
import {createFeature, createReducer, createSelector, on} from '@ngrx/store'
import {
  createUser,
  fetchUserInfo,
  loggedOut,
  logout,
  markedNotificationAsRead,
  markNotificationAsRead,
  rejectLogin,
  resetError,
  updateNotifications,
  userCreated,
  userInfoFetched,
  userLoggedIn,
  userLogin
} from './user.actions'
import {Role} from '../../model/user/role'
import {Notification} from '../model/notification'

export interface UserState {
  user: User | null
  notifications: Notification[]
  isUserLoading: boolean
  error: string | null
}

export const initialState: UserState = {
  user: null,
  isUserLoading: false,
  notifications: [],
  error: null
}

export const userFeature = createFeature({
  name: 'user',
  reducer: createReducer(
    initialState,
    on(createUser, (state): UserState => {
      return {
        ...state,
        isUserLoading: true
      }
    }),
    on(userCreated, markNotificationAsRead, (state): UserState => {
      return {
        ...state,
        isUserLoading: false
      }
    }),
    on(resetError, (state): UserState => {
      return {
        ...state,
        error: null
      }
    }),
    on(userLogin, (state): UserState => {
      return {
        ...state,
        isUserLoading: true,
        error: null
      }
    }),
    on(userLoggedIn, (state, action): UserState => {
      return {
        ...state,
        user: action,
        isUserLoading: false
      }
    }),
    on(rejectLogin, (state): UserState => {
      return {
        ...state,
        isUserLoading: false,
        error: 'Username oder Passwort ungültig'
      }
    }),
    on(fetchUserInfo, (state): UserState => {
      return {
        ...state,
        isUserLoading: true
      }
    }),
    on(userInfoFetched, (state, action): UserState => {
      return {
        ...state,
        isUserLoading: false,
        user: action.user.id ? action.user : null
      }
    }),
    on(updateNotifications, (state, action): UserState => {
      return {
        ...state,
        notifications: action.notifications
      }
    }),
    on(logout, (state): UserState => {
      return {
        ...state,
        isUserLoading: true
      }
    }),
    on(loggedOut, (state): UserState => {
      return {
        ...state,
        isUserLoading: false,
        user: null,
        notifications: []
      }
    }),
    on(markedNotificationAsRead, (state): UserState => {
      return {
        ...state,
        isUserLoading: false
      }
    })
  )
})

export const {
  selectIsUserLoading,
  selectUser,
  selectError,
  selectNotifications
} = userFeature

export const selectIsAdmin = createSelector(
  selectUser,
  (user: User | null): boolean => {
    return !!user && user.role === Role.ADMIN
  }
)
