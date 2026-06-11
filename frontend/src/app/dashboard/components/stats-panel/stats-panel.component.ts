import {Component, computed, input, Signal, signal} from '@angular/core'
import {Statistics} from '../../../model/dashboard/statistics'
import {GlobalStatistics} from '../../../model/dashboard/global-statistics'

interface StatItem {
  icon: string
  key: string
  value: number
}

@Component({
  selector: 'wm-stats-panel',
  imports: [],
  templateUrl: './stats-panel.component.html',
  styleUrl: './stats-panel.component.css'
})
export class StatsPanelComponent {
  personalStats = input<Statistics>()
  globalStats = input<GlobalStatistics>()

  showGlobalStats = signal<boolean>(false)

  stats: Signal<StatItem[]> = computed(() => {
    const globalStats = this.globalStats()
    const personalStats = this.personalStats()

    if (!globalStats || !personalStats) return []

    if (this.showGlobalStats()) {
      return [
        {icon: 'check_circle', key: 'Richtige Tipps', value: globalStats.correctGames},
        {icon: 'stars', key: 'Erhaltene Punkte', value: globalStats.totalPoints},
        {icon: 'star_half', key: 'Verlorene Joker', value: globalStats.jokersWasted}
      ]
    }
    return [
      {icon: 'check_circle', key: 'Richtige Tipps', value: personalStats.correctGames},
      {icon: 'sports_soccer', key: 'Getippte Tore', value: personalStats.totalGoalsBet},
      {icon: 'star_half', key: 'Verlorene Joker', value: personalStats.jokersWasted}
    ]
  })
}
