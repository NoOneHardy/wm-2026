import {inject, Injectable} from '@angular/core'
import {HttpClient} from '@angular/common/http'
import {Observable} from 'rxjs'
import {User} from '../model/user/user'

@Injectable({
  providedIn: 'root'
})
export class AdminService {
  private http = inject(HttpClient)

  getAllUsers(): Observable<User[]> {
    return this.http.get<User[]>('/api/user/all')
  }

  confirmUser(id: string): Observable<User> {
    return this.http.get<User>(`/api/admin/user/${id}/confirm`)
  }

  denyUser(id: string): Observable<User> {
    return this.http.get<User>(`/api/admin/user/${id}/deny`)
  }
}
