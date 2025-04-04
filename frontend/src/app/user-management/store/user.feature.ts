import {User} from '../../model/user/user'
import {createFeature, createReducer, on} from '@ngrx/store'
import {
  createUser,
  fetchUserInfo,
  loggedOut, logout,
  userCreated,
  userInfoFetched,
  userLoggedIn,
  userLogin
} from './user.actions'

interface UserState {
  user: User | null
  isLoading: boolean
}

const initialState: UserState = {
  user: null,
  isLoading: false
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
    on(userLogin, (state): UserState => {
      return {
        ...state,
        isLoading: true
      }
    }),
    on(userLoggedIn, (state, action): UserState => {
      return {
        ...state,
        user: action,
        isLoading: false
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
        user: action.user
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
  selectUser
} = userFeature
