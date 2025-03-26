import {User} from '../../model/user/user'
import {createFeature, createReducer, on} from '@ngrx/store'
import {createUser, userCreated} from './user.actions'

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
    })
  )
})

export const {
  selectIsLoading,
  selectToken
} = userFeature
