import {Component, computed, input, InputSignal, signal, Signal, WritableSignal} from '@angular/core'
import {Statistics} from '../../../model/dashboard/statistics'
import {GlobalStatistics} from '../../../model/dashboard/global-statistics'
import {CommonModule} from '@angular/common'

@Component({
  selector: 'wm-statistics',
  standalone: true,
  imports: [
    CommonModule
  ],
  templateUrl: './statistics.component.html',
  styleUrl: './statistics.component.css'
})
export class StatisticsComponent {
  personalStats: InputSignal<Statistics> = input.required<Statistics>()
  globalStats: InputSignal<GlobalStatistics> = input.required<GlobalStatistics>()

  stats: Signal<{ key: string; value: number }[]> = computed(() => {
    const globalStats = this.globalStats()
    const personalStats = this.personalStats()

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

  showGlobalStats: WritableSignal<boolean> = signal<boolean>(false)

  toggleGlobalStats(): void {
    this.showGlobalStats.update(v => !v)
  }
}
