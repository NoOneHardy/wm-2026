import {inject, Injectable} from '@angular/core'
import {HttpClient} from '@angular/common/http'
import {Ranking} from '../../../model/leaderboard/ranking'
import {Observable} from 'rxjs'

@Injectable({
  providedIn: 'root'
})
export class LeaderboardService {
  private http = inject(HttpClient)

  getLeaderboard(): Observable<Ranking[]> {
    return this.http.get<Ranking[]>('/api/leaderboard')
  }
}
