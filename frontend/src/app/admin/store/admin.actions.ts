import {createAction, props} from '@ngrx/store'
import {User} from '../../model/user/user'

export const loadUsers = createAction('[Admin] Load a list of users')
export const usersLoaded = createAction('[Admin] User list loaded', props<{ users: User[] }>())
