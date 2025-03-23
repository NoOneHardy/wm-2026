import {inject, Injectable} from '@angular/core'
import {HttpClient} from '@angular/common/http'
import {Observable} from 'rxjs'
import {AvailabilityCheck} from './model/availability-check'

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
}
