import {createAction, props} from '@ngrx/store'
import {NewUser, User} from '../../model/user/user'
import {LoginData} from '../model/login'

export const createUser = createAction('[User] create user', props<{user: NewUser}>())
export const userCreated = createAction('[User] user created', props<LoginData>())
export const userLogin = createAction('[User] login', props<LoginData>())
export const fetchUserInfo = createAction('[User] Get user data')
export const userLoggedIn = createAction('[User] login finished', props<User>())
export const userInfoFetched = createAction('[User] user data fetched', props<{user: User}>())
export const logout = createAction('[User] Logout')
export const loggedOut = createAction('[User] Logged out')
