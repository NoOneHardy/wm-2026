import {Injectable} from '@angular/core'
import {Observable} from 'rxjs'
import {AvailabilityCheck} from './model/availability-check'
import {NewUser, User} from '../model/user/user'
import {LoginData} from './model/login'
import {BaseHttpService} from '../shared/services/base-http/base-http.service'

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
}
