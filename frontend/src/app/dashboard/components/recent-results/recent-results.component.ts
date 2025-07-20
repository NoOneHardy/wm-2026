import {Component, input} from '@angular/core'
import {BetGame} from '../../../model/game/bet-game'
import {GamePreviewComponent} from '../../../shared/components/game/game-preview/game-preview.component'
import {CommonModule} from '@angular/common'
import {RouterLink} from '@angular/router'

@Component({
  selector: 'wm-recent-results',
  standalone: true,
  imports: [
    CommonModule,
    GamePreviewComponent,
    RouterLink
  ],
  templateUrl: './recent-results.component.html',
  styleUrl: './recent-results.component.css'
})
export class RecentResultsComponent {
  games = input.required<BetGame[], BetGame[]>({
    transform: v => {
      const sorted = [...v]
      sorted.sort((a, b) => {
        return new Date(b.timestamp).valueOf() - new Date(a.timestamp).valueOf()
      })
      return sorted
    }
  })
}
