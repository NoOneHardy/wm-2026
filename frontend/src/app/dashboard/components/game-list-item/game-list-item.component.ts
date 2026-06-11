import {Component, computed, inject, input, Signal} from '@angular/core'
import {DatePipe, NgOptimizedImage} from '@angular/common'
import {Router} from '@angular/router'
import {BetGame} from '../../../model/game/bet-game'
import {PointService} from '../../../shared/services/point/point.service'

@Component({
  selector: 'wm-game-list-item',
  imports: [
    DatePipe,
    NgOptimizedImage
  ],
  templateUrl: './game-list-item.component.html',
  styleUrl: './game-list-item.component.css'
})
export class GameListItemComponent {
  private router = inject(Router)
  private pointService = inject(PointService)

  game = input.required<BetGame>()

  points: Signal<number> = computed(() => this.pointService.calculatePoints(this.game()))

  openGame(): void {
    const game = this.game()
    this.router.navigateByUrl(`/bets/${game.groupId}/${game.id}`).then()
  }
}
