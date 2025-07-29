import {Injectable} from '@angular/core'
import {Observable} from 'rxjs'
import {User} from '../model/user/user'
import {BaseHttpService} from '../shared/services/base-http/base-http.service'

@Injectable({
  providedIn: 'root'
})
export class AdminService extends BaseHttpService {
  getAllUsers(): Observable<User[]> {
    return this.get<User[]>('/api/user/all')
  }

  confirmUser(id: string): Observable<User> {
    return this.get<User>(`/api/admin/user/${id}/confirm`)
  }

  denyUser(id: string): Observable<User> {
    return this.get<User>(`/api/admin/user/${id}/deny`)
  }
}
