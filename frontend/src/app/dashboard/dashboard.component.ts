import {Component, computed, inject, OnInit} from '@angular/core'
import {Store} from '@ngrx/store'
import {selectUser} from '../user-management/store/user.feature'
import {PositionComponent} from '../shared/components/position/position.component'
import {DecimalPipe, NgForOf, NgIf} from '@angular/common'
import {selectDashboard} from '../shared/store/tournament.feature'
import {loadDashboardData} from '../shared/store/tournament.actions'
import {GamePreviewComponent} from '../shared/components/game/game-preview/game-preview.component'

@Component({
  selector: 'wm-dashboard',
  standalone: true,
  imports: [
    PositionComponent,
    NgForOf,
    NgIf,
    DecimalPipe,
    GamePreviewComponent
  ],
  templateUrl: './dashboard.component.html',
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

  dashboardData = this.store.selectSignal(selectDashboard)
  leaderboardPreview = computed(() => {
    return this.dashboardData()?.leaderboardPreview.filter((item) => !!item) ?? []
  })

  ngOnInit(): void {
    this.store.dispatch(loadDashboardData())
  }
}
