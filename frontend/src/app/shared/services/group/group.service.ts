import {Injectable} from '@angular/core'
import {CardGroup} from '../../../model/group/card-group'
import {Observable} from 'rxjs'
import {Group} from '../../../model/group/group'
import {BetForm} from '../../../model/game/bet-form'
import {ScoreForm} from '../../../model/game/score-form'
import {BaseHttpService} from '../base-http/base-http.service'

@Injectable({
  providedIn: 'root'
})
export class GroupService extends BaseHttpService {
  public getGroups(): Observable<CardGroup[]> {
    return this.get<CardGroup[]>('/api/group')
  }

  public getGroup(groupId: string): Observable<Group> {
    return this.get<Group>(`/api/group/${groupId}`)
  }

  public saveBets(groupId: string, bets: BetForm[]): Observable<Group> {
    return this.put<Group, BetForm[]>(`/api/group/${groupId}/bets`, bets, {
      headers: {
        'Content-Type': 'application/json'
      }
    })
  }

  public saveResults(groupId: string, results: ScoreForm[]): Observable<Group> {
    return this.put<Group, ScoreForm[]>(`/api/group/${groupId}/results`, results, {
      headers: {
        'Content-Type': 'application/json'
      }
    })
  }
}
