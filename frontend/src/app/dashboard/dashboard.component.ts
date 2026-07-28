import {Component, computed, inject, OnInit, ChangeDetectionStrategy} from '@angular/core'
import {Store} from '@ngrx/store'
import {selectUser} from '../user-management/store/user.feature'
import {PositionComponent} from '../shared/components/position/position.component'
import { DecimalPipe } from '@angular/common'
import {selectDashboard, selectIsTournamentLoading} from '../shared/store/tournament.feature'
import {loadDashboardData} from '../shared/store/tournament.actions'
import {UpcomingGamesComponent} from './components/upcoming-games/upcoming-games.component'
import {StatisticsComponent} from './components/statistics/statistics.component'
import {RecentResultsComponent} from './components/recent-results/recent-results.component'
import {SpinnerComponent} from '../shared/components/spinner/spinner.component'

@Component({
  selector: 'bet-dashboard',
  imports: [
    PositionComponent,
    DecimalPipe,
    UpcomingGamesComponent,
    StatisticsComponent,
    RecentResultsComponent,
    SpinnerComponent
  ],
  templateUrl: './dashboard.component.html',
  changeDetection: ChangeDetectionStrategy.Eager,
  styleUrl: './dashboard.component.css'
})
export class DashboardComponent implements OnInit {
  private store = inject(Store)

  user = this.store.selectSignal(selectUser)

  greeting = computed(() => {
    const time = new Date().getHours()
    let greeting: string

    if (time >= 3 && time < 10) greeting = 'Guten Morgen'
    else if (time >= 10 && time < 18) greeting = 'Hallo'
    else greeting = 'Guten Abend'

    return greeting + ' ' + this.user()?.username
  })

  isLoading = this.store.selectSignal(selectIsTournamentLoading)

  dashboardData = this.store.selectSignal(selectDashboard)
  leaderboardPreview = computed(() => {
    return this.dashboardData()?.leaderboardPreview.filter((item) => !!item) ?? []
  })

  ngOnInit(): void {
    this.store.dispatch(loadDashboardData())
  }
}
