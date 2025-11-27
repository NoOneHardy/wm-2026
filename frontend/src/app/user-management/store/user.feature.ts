import {User} from '../../model/user/user'
import {createFeature, createReducer, createSelector, on} from '@ngrx/store'
import {
  createUser,
  fetchNotificationPreferences,
  fetchUserInfo,
  loggedOut,
  logout,
  markedNotificationAsRead,
  markNotificationAsRead,
  notificationPreferencesFetched,
  notificationPreferencesUpdated,
  rejectLogin,
  resetError,
  updateNotificationPreferences,
  updateNotifications,
  updateUser,
  userCreated,
  userInfoFetched,
  userLoggedIn,
  userLogin,
  userUpdated
} from './user.actions'
import {Role} from '../../model/user/role'
import {Notification} from '../model/notification'
import {NotificationPreference} from '../model/notification-preference'

interface Preferences {
  notifications: NotificationPreference[]
}

export interface UserState {
  user: User | null
  notifications: Notification[]
  preferences: Preferences | null
  isUserLoading: boolean
  error: string | null
}

export const initialState: UserState = {
  user: null,
  isUserLoading: false,
  notifications: [],
  preferences: null,
  error: null
}

export const userFeature = createFeature({
  name: 'user',
  reducer: createReducer(
    initialState,
    on(createUser, markNotificationAsRead, updateNotificationPreferences, fetchNotificationPreferences, updateUser, (state): UserState => {
      return {
        ...state,
        isUserLoading: true
      }
    }),
    on(userCreated, (state): UserState => {
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
    on(userInfoFetched, userUpdated, (state, action): UserState => {
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
    }),
    on(notificationPreferencesFetched, notificationPreferencesUpdated, (state, action): UserState => {
      return {
        ...state,
        isUserLoading: false,
        preferences: {
          ...state.preferences,
          notifications: action.preferences
        }
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

export const selectNotificationPreferences = createSelector(
  userFeature.selectPreferences,
  (preferences: Preferences | null): NotificationPreference[] => {
    return preferences ? preferences.notifications : []
  }
)
