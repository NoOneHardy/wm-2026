import {createAction, props} from '@ngrx/store'
import {NewUser, UpdateUser, User} from '../../model/user/user'
import {LoginData} from '../model/login'
import {Notification} from '../model/notification'
import {NotificationPreference} from '../model/notification-preference'

// General
export const resetUserLoading = createAction('[User] Reset user loading')

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
export const updateUser = createAction('[User] Update user', props<{ user: UpdateUser }>())
export const userUpdated = createAction('[User] User updated', props<{ user: User }>())

// Preferences
export const updateNotificationPreferences = createAction('[User Preferences] Update notification preferences', props<{
  preferences: NotificationPreference[]
}>())
export const notificationPreferencesUpdated = createAction('[User Preferences] Notification preferences updated', props<{
  preferences: NotificationPreference[]
}>())
export const fetchNotificationPreferences = createAction('[User Preferences] Fetch notification preferences')
export const notificationPreferencesFetched = createAction('[User Preferences] Notification preferences fetched', props<{
  preferences: NotificationPreference[]
}>())

// Email verification
export const verifyEmail = createAction('[User] Verify email', props<{ code: string }>())
export const emailVerified = createAction('[User] Email verified', props<{ user: User }>())
export const sendEmailVerificationLink = createAction('[User] Send email verification link')
export const emailVerificationLinkSent = createAction('[User] Email verification link sent', props<{ isSent: boolean }>())

// Password reset
export const requestPasswordResetLink = createAction('[User] Request password reset link', props<{ email: string }>())
export const passwordResetLinkRequested = createAction('[User] Password reset link requested')
export const resetPassword = createAction('[User] Reset password', props<{ newPassword: string, code: string }>())
export const passwordResetDone = createAction('[User] Password reset done', props<{ success: boolean }>())
