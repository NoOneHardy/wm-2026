import {Injectable} from '@angular/core'
import {Observable} from 'rxjs'
import {User} from '../model/user/user'
import {BaseHttpService} from '../shared/services/base-http/base-http.service'
import {LightTeam} from '../model/team/light-team'
import {GroupOption} from './model/group-option'
import {CreateTeam} from './model/create-team'
import {CreateGroup} from './model/create-group'
import {CreateGame} from './model/create-game'
import {BetGame} from '../model/game/bet-game'

@Injectable({
  providedIn: 'root'
})
export class AdminService extends BaseHttpService {
  getUsers(): Observable<User[]> {
    return this.get<User[]>('/api/user')
  }

  confirmUser(id: string): Observable<User> {
    return this.get<User>(`/api/admin/user/${id}/confirm`)
  }

  denyUser(id: string): Observable<User> {
    return this.get<User>(`/api/admin/user/${id}/deny`)
  }

  getTeams(): Observable<LightTeam[]> {
    return this.get<LightTeam[]>('/api/team')
  }

  getGroupOptions(): Observable<GroupOption[]> {
    return this.get<GroupOption[]>('/api/group/all')
  }

  uploadFlag(file: File): Observable<string> {
    const formData = new FormData()
    formData.append('file', file)

    return this.post<string, FormData>('/api/team/flag', formData)
  }

  createTeam(team: CreateTeam): Observable<LightTeam> {
    return this.post<LightTeam, CreateTeam>('/api/team', team)
  }

  createGroup(group: CreateGroup): Observable<GroupOption> {
    return this.post<GroupOption, CreateGroup>('/api/group', group)
  }

  createGame(game: CreateGame): Observable<BetGame> {
    return this.post<BetGame, CreateGame>('/api/game', game)
  }
}
