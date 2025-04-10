import {User} from '../../model/user/user'
import {createFeature, createReducer, on} from '@ngrx/store'
import {
  createUser,
  fetchUserInfo,
  loggedOut, logout, rejectLogin, resetError,
  userCreated,
  userInfoFetched,
  userLoggedIn,
  userLogin
} from './user.actions'

interface UserState {
  user: User | null
  isLoading: boolean
  error: string | null
}

const initialState: UserState = {
  user: null,
  isLoading: false,
  error: null
}

export const userFeature = createFeature({
  name: 'user',
  reducer: createReducer(
    initialState,
    on(createUser, (state): UserState => {
      return {
        ...state,
        isLoading: true
      }
    }),
    on(userCreated, (state): UserState => {
      return {
        ...state,
        isLoading: false
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
        isLoading: true,
        error: null
      }
    }),
    on(userLoggedIn, (state, action): UserState => {
      return {
        ...state,
        user: action,
        isLoading: false
      }
    }),
    on(rejectLogin, (state): UserState => {
      return {
        ...state,
        isLoading: false,
        error: 'Username oder Password ungültig'
      }
    }),
    on(fetchUserInfo, (state): UserState => {
      return {
        ...state,
        isLoading: true
      }
    }),
    on(userInfoFetched, (state, action): UserState => {
      return {
        ...state,
        isLoading: false,
        user: action.user.id ? action.user : null
      }
    }),
    on(logout, (state): UserState => {
      return {
        ...state,
        isLoading: true
      }
    }),
    on(loggedOut, (state) => {
      return {
        ...state,
        isLoading: false,
        user: null
      }
    })
  )
})

export const {
  selectIsLoading,
  selectUser,
  selectError
} = userFeature
