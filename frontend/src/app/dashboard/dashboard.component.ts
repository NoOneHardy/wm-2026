import {Component, computed, inject, OnInit, Signal} from '@angular/core'
import {DatePipe, DecimalPipe} from '@angular/common'
import {RouterLink} from '@angular/router'
import {Store} from '@ngrx/store'
import {selectUser} from '../user-management/store/user.feature'
import {selectDashboard, selectIsTournamentLoading} from '../shared/store/tournament.feature'
import {loadDashboardData} from '../shared/store/tournament.actions'
import {Ranking} from '../model/leaderboard/ranking'
import {BetGame} from '../model/game/bet-game'
import {PositionComponent} from '../shared/components/position/position.component'
import {SpinnerComponent} from '../shared/components/spinner/spinner.component'
import {StatCardComponent} from './components/stat-card/stat-card.component'
import {NextKickoffComponent} from './components/next-kickoff/next-kickoff.component'
import {GameListItemComponent} from './components/game-list-item/game-list-item.component'
import {StatsPanelComponent} from './components/stats-panel/stats-panel.component'

@Component({
  selector: 'wm-dashboard',
  imports: [
    DatePipe,
    DecimalPipe,
    RouterLink,
    PositionComponent,
    SpinnerComponent,
    StatCardComponent,
    NextKickoffComponent,
    GameListItemComponent,
    StatsPanelComponent
  ],
  templateUrl: './dashboard.component.html',
  styleUrl: './dashboard.component.css'
})
export class DashboardComponent implements OnInit {
  private store = inject(Store)

  user = this.store.selectSignal(selectUser)
  isLoading = this.store.selectSignal(selectIsTournamentLoading)
  dashboardData = this.store.selectSignal(selectDashboard)

  today = new Date()

  greeting = computed(() => {
    const time = new Date().getHours()

    if (time >= 3 && time < 10) return 'Guten Morgen'
    if (time >= 10 && time < 18) return 'Hallo'
    return 'Guten Abend'
  })

  leaderboardPreview: Signal<Ranking[]> = computed(() => {
    return this.dashboardData()?.leaderboardPreview.filter((item): item is Ranking => !!item) ?? []
  })

  upcomingGames: Signal<BetGame[]> = computed(() => {
    const games = [...(this.dashboardData()?.upcomingGames ?? [])]
    games.sort((a, b) => new Date(a.timestamp).valueOf() - new Date(b.timestamp).valueOf())
    return games
  })

  nextGame: Signal<BetGame | null> = computed(() => this.upcomingGames()[0] ?? null)

  laterGames: Signal<BetGame[]> = computed(() => this.upcomingGames().slice(1))

  recentResults: Signal<BetGame[]> = computed(() => {
    const games = [...(this.dashboardData()?.recentResults ?? [])]
    games.sort((a, b) => new Date(b.timestamp).valueOf() - new Date(a.timestamp).valueOf())
    return games
  })

  openBetCount: Signal<number> = computed(() => {
    return this.upcomingGames().filter(game => !game.bet).length
  })

  ngOnInit(): void {
    this.store.dispatch(loadDashboardData())
  }
}
