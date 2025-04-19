import {inject, Injectable} from '@angular/core'
import {HttpClient} from '@angular/common/http'
import {CardGroup} from '../../../model/group/card-group'
import {Observable} from 'rxjs'
import {Group} from '../../../model/group/group'

@Injectable({
  providedIn: 'root'
})
export class GroupService {
  private http = inject(HttpClient)

  public getGroups(): Observable<CardGroup[]> {
    return this.http.get<CardGroup[]>('/api/group')
  }

  public getGroup(groupId: string): Observable<Group> {
    return this.http.get<Group>(`/api/group/${groupId}`)
  }
}
