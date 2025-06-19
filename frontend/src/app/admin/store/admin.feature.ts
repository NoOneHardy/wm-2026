import {User} from '../../model/user/user'
import {createFeature, createReducer, on} from '@ngrx/store'
import {applicationReviewed, confirmUser, denyUser, loadUsers, usersLoaded} from './admin.actions'

export interface AdminState {
  users: User[]
  isAdminLoading: boolean
}

export const initialState: AdminState = {
  users: [],
  isAdminLoading: false
}

export const adminFeature = createFeature({
  name: 'admin',
  reducer: createReducer(
    initialState,
    on(loadUsers,
      confirmUser,
      denyUser,
      (state): AdminState => {
        return {
          ...state,
          isAdminLoading: true
        }
      }),
    on(usersLoaded, (state, action): AdminState => {
      return {
        ...state,
        isAdminLoading: false,
        users: action.users
      }
    }),
    on(applicationReviewed, (state, action): AdminState => {
      return {
        ...state,
        isAdminLoading: false,
        users: [
          ...state.users.filter(user => user.id !== action.user.id),
          action.user
        ]
      }
    })
  )
})

export const {
  selectUsers
} = adminFeature
