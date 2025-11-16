import {createAction, props} from '@ngrx/store'
import {NewUser, User} from '../../model/user/user'
import {LoginData} from '../model/login'
import {Notification} from '../model/notification'

export const createUser = createAction('[User] create user', props<{ user: NewUser }>())
export const userCreated = createAction('[User] user created', props<LoginData>())
export const userLogin = createAction('[User] login', props<LoginData>())
export const rejectLogin = createAction('[User] login rejected')
export const resetError = createAction('[User] Reset error')
export const fetchUserInfo = createAction('[User] Get user data')
export const userLoggedIn = createAction('[User] login finished', props<User>())
export const userInfoFetched = createAction('[User] user data fetched', props<{ user: User }>())
export const logout = createAction('[User] Logout')
export const loggedOut = createAction('[User] Logged out', props<{ showMessage: boolean }>())
export const updateNotifications = createAction('[User] Update notifications', props<{
  notifications: Notification[]
}>())
export const markNotificationAsRead = createAction('[User] Mark notification as read', props<{ id: string }>())
export const markedNotificationAsRead = createAction('[User] Mark notification as read DONE')
export const uploadAvatar = createAction('[User] Upload avatar', props<{ file: File }>())
export const avatarUploaded = createAction('[User] Avatar uploaded', props<{ avatarUrl?: string | null }>())
