import {Component, computed, inject, input, signal, Signal} from '@angular/core'
import {Statistics} from '../../../model/dashboard/statistics'
import {GlobalStatistics} from '../../../model/dashboard/global-statistics'
import {CommonModule} from '@angular/common'
import {SpinnerComponent} from '../../../shared/components/spinner/spinner.component'
import {Store} from '@ngrx/store'
import {selectIsTournamentLoading} from '../../../shared/store/tournament.feature'

@Component({
  selector: 'wm-statistics',
  standalone: true,
  imports: [
    CommonModule,
    SpinnerComponent
  ],
  templateUrl: './statistics.component.html',
  styleUrl: './statistics.component.css'
})
export class StatisticsComponent {
  private store = inject(Store)

  personalStats = input<Statistics>()
  globalStats = input<GlobalStatistics>()

  stats: Signal<{ key: string; value: number }[]> = computed(() => {
    const globalStats = this.globalStats()
    const personalStats = this.personalStats()

    if (!globalStats || !personalStats) return []

    if (this.showGlobalStats()) {
      return [
        {key: 'Richtige Tipps', value: globalStats.correctGames},
        {key: 'Erhaltene Punkte', value: globalStats.totalPoints},
        {key: 'Verlorene Joker', value: globalStats.jokersWasted},
      ]
    }
    return [
      {key: 'Richtige Tipps', value: personalStats.correctGames},
      {key: 'Getippte Goals', value: personalStats.totalGoalsBet},
      {key: 'Verlorene Joker', value: personalStats.jokersWasted},
    ]
  })

  showGlobalStats = signal<boolean>(false)

  isLoading = this.store.selectSignal(selectIsTournamentLoading)
}
