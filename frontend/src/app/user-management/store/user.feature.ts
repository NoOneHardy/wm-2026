import {User} from '../../model/user/user'
import {createFeature, createReducer, on} from '@ngrx/store'
import {createUser, fetchUserInfo, logout, userCreated, userInfoFetched, userLoggedIn, userLogin} from './user.actions'

interface UserState {
  user: User | null
  isLoading: boolean
  token: string | null
}

const initialState: UserState = {
  user: null,
  token: null,
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
        token: action.token,
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
    on(logout, () => initialState)
  )
})

export const {
  selectIsLoading,
  selectToken,
  selectUser
} = userFeature
