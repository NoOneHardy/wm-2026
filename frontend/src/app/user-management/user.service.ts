import {Injectable} from '@angular/core'
import {Observable} from 'rxjs'
import {AvailabilityCheck} from './model/availability-check'
import {NewUser, User} from '../model/user/user'
import {LoginData, LoginResponse} from './model/login'
import {AuthHttpService} from '../shared/services/auth-http.service'

@Injectable({
  providedIn: 'root'
})
export class UserService extends AuthHttpService {
  checkUsername(username?: string): Observable<AvailabilityCheck> {
    const params = new URLSearchParams()
    if (username) params.set('username', username)
    return this.http.get<AvailabilityCheck>('/api/user/check?' + params.toString())
  }

  checkEmail(email?: string): Observable<AvailabilityCheck> {
    const params = new URLSearchParams()
    if (email) params.set('email', email)
    return this.http.get<AvailabilityCheck>('/api/user/check?' + params.toString())
  }

  createUser(user: NewUser): Observable<User> {
    return this.http.post<User>('/api/signup', user)
  }

  login(data: LoginData): Observable<LoginResponse> {
    return this.http.post<LoginResponse>('/api/login', data)
  }

  fetchUserInfo(): Observable<User> {
    return this.get<User>('/api/me')
  }
}
