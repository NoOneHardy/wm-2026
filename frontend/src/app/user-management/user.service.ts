import {inject, Injectable} from '@angular/core'
import {Observable} from 'rxjs'
import {AvailabilityCheck} from './model/availability-check'
import {NewUser, User} from '../model/user/user'
import {LoginData} from './model/login'
import {HttpClient} from '@angular/common/http'

@Injectable({
  providedIn: 'root'
})
export class UserService {
  private http = inject(HttpClient)

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

  login(data: LoginData): Observable<User> {
    return this.http.post<User>('/api/login', data)
  }

  logout(): Observable<void> {
    return this.http.post<void>('/api/log-out', {})
  }

  fetchUserInfo(): Observable<User> {
    return this.http.get<User>('/api/me')
  }
}
