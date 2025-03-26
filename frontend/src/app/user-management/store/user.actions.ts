import {createAction, props} from '@ngrx/store'
import {NewUser} from '../../model/user/user'

export const createUser = createAction('[User] create user', props<{user: NewUser}>())

export const userCreated = createAction('[User] user created')
