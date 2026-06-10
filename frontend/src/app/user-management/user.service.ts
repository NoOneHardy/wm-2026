import {Injectable} from '@angular/core'
import {Observable} from 'rxjs'
import {AvailabilityCheck} from './model/availability-check'
import {NewUser, UpdateUser, User} from '../model/user/user'
import {LoginData} from './model/login'
import {BaseHttpService} from '../shared/services/base-http/base-http.service'
import {NotificationPreference} from './model/notification-preference'

@Injectable({
  providedIn: 'root'
})
export class UserService extends BaseHttpService {
  checkUsername(username?: string): Observable<AvailabilityCheck> {
    const params = new URLSearchParams()
    if (username) params.set('username', username)
    return this.get<AvailabilityCheck>('/api/user/check?' + params.toString())
  }

  checkEmail(email?: string): Observable<AvailabilityCheck> {
    const params = new URLSearchParams()
    if (email) params.set('email', email)
    return this.get<AvailabilityCheck>('/api/user/check?' + params.toString())
  }

  createUser(user: NewUser): Observable<User> {
    return this.post<User, NewUser>('/api/signup', user)
  }

  login(data: LoginData): Observable<User> {
    return this.post<User, LoginData>('/api/login', data)
  }

  logout(): Observable<void> {
    return this.post<void>('/api/log-out', {})
  }

  fetchUserInfo(): Observable<User> {
    return this.get<User>('/api/me')
  }

  markNotificationAsRead(id: string): Observable<true> {
    return this.delete<true>(`/api/notification/${id}`)
  }

  uploadAvatar(file: File): Observable<User> {
    const formData = new FormData()
    formData.append('file', file)

    return this.put('/api/me/avatar', formData)
  }

  updateUser(user: UpdateUser): Observable<User> {
    return this.put<User>(`/api/user/me`, user)
  }

  fetchNotificationPreferences(): Observable<NotificationPreference[]> {
    return this.get<NotificationPreference[]>('/api/preferences/notifications')
  }

  updateNotificationPreferences(preferences: NotificationPreference[]): Observable<NotificationPreference[]> {
    return this.put<NotificationPreference[]>('/api/preferences/notifications', preferences)
  }

  verifyEmail(body: { code: string }): Observable<User> {
    return this.post<User>('/api/user/confirm', body)
  }

  sendEmailVerificationLink(): Observable<boolean> {
    return this.put<boolean>('/api/user/resend-verification', {})
  }

  requestPasswordResetLink(email: string): Observable<boolean> {
    return this.put<boolean>('/api/user/password-reset/request-link', {}, {
      params: {email}
    })
  }

  resetPassword(newPassword: string, code: string): Observable<boolean> {
    return this.put<boolean>('/api/user/password-reset', {newPassword, code})
  }
}
