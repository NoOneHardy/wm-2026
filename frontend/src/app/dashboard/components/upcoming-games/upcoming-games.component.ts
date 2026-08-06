import {Component, input, ChangeDetectionStrategy} from '@angular/core'
import {BetGame} from '../../../model/game/bet-game'
import {GamePreviewComponent} from '../../../shared/components/game/game-preview/game-preview.component'

import {RouterLink} from '@angular/router'

@Component({
  selector: 'bet-upcoming-games',
  imports: [
    GamePreviewComponent,
    RouterLink
  ],
  templateUrl: './upcoming-games.component.html',
  changeDetection: ChangeDetectionStrategy.Eager,
  styleUrl: './upcoming-games.component.css'
})
export class UpcomingGamesComponent {
  games = input.required<BetGame[], BetGame[]>({
    transform: v => {
      const sorted = [...v]
      sorted.sort((a, b) => {
        return new Date(a.timestamp).valueOf() - new Date(b.timestamp).valueOf()
      })
      return sorted
    }
  })
}
