import {createAction, props} from '@ngrx/store'
import {User} from '../../model/user/user'

export const loadUsers = createAction('[Admin] Load a list of users')
export const usersLoaded = createAction('[Admin] User list loaded', props<{ users: User[] }>())
export const confirmUser = createAction('[Admin] Confirm user application', props<{ id: string }>())
export const denyUser = createAction('[Admin] Deny user application', props<{ id: string }>())
export const applicationReviewed = createAction('[Admin] User application reviewed', props<{ user: User }>())
