import {User} from '../../model/user/user'
import {createFeature, createReducer, createSelector, on} from '@ngrx/store'
import {
  createUser,
  fetchUserInfo,
  loggedOut,
  logout,
  rejectLogin,
  resetError,
  userCreated,
  userInfoFetched,
  userLoggedIn,
  userLogin
} from './user.actions'
import {Role} from '../../model/user/role'

interface UserState {
  user: User | null
  isUserLoading: boolean
  error: string | null
}

const initialState: UserState = {
  user: null,
  isUserLoading: false,
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
    on(userInfoFetched, (state, action): UserState => {
      return {
        ...state,
        isUserLoading: false,
        user: action.user.id ? action.user : null
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
        user: null
      }
    })
  )
})

export const {
  selectIsUserLoading,
  selectUser,
  selectError
} = userFeature

export const selectIsAdmin = createSelector(
  selectUser,
  (user: User | null): boolean => {
    return !!user && user.role === Role.ADMIN
  }
)
