import {Injectable} from '@angular/core'
import {Ranking} from '../../../model/leaderboard/ranking'
import {Observable} from 'rxjs'
import {BaseHttpService} from '../base-http/base-http.service'

@Injectable({
  providedIn: 'root'
})
export class LeaderboardService extends BaseHttpService{
  getLeaderboard(): Observable<Ranking[]> {
    return this.get<Ranking[]>('/api/leaderboard')
  }
}
