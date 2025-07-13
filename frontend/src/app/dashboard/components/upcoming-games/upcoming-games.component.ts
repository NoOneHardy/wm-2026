import {Component, input} from '@angular/core'
import {BetGame} from '../../../model/game/bet-game'
import {GamePreviewComponent} from '../../../shared/components/game/game-preview/game-preview.component'
import {CommonModule} from '@angular/common'
import {RouterLink} from '@angular/router'

@Component({
  selector: 'wm-upcoming-games',
  standalone: true,
  imports: [
    CommonModule,
    GamePreviewComponent,
    RouterLink
  ],
  templateUrl: './upcoming-games.component.html',
  styleUrl: './upcoming-games.component.css'
})
export class UpcomingGamesComponent {
  games = input.required<BetGame[]>()
}
