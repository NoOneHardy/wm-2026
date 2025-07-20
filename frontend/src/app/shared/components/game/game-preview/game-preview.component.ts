import {Component, computed, inject, input, Signal} from '@angular/core'
import {BetGame} from '../../../../model/game/bet-game'
import {DatePipe, NgIf, NgOptimizedImage} from '@angular/common'
import {ButtonComponent} from '../../button/button.component'
import {Router} from '@angular/router'
import {PointService} from '../../../services/point/point.service'
import {Bet} from '../../../../model/game/bet'

@Component({
  selector: 'wm-game-preview',
  standalone: true,
  imports: [
    DatePipe,
    NgOptimizedImage,
    ButtonComponent,
    NgIf
  ],
  templateUrl: './game-preview.component.html',
  styleUrl: './game-preview.component.css'
})
export class GamePreviewComponent {
  private router = inject(Router)
  private pointService = inject(PointService)

  game = input.required<BetGame>()

  openBet: Signal<boolean> = computed(() => !this.game().bet && !this.game().result)
  points: Signal<number> = computed(() => {
    return this.pointService.calculatePoints(this.game())
  })

  focusGame(): void {
    const id = this.game().id
    const groupId = this.game().groupId
    this.router.navigateByUrl(`/bets/${groupId}/${id}`).then()
  }

  getJokerArray(bet: Bet): number[] {
    return Array(bet.joker).map((_, i) => i + 1)
  }
}
