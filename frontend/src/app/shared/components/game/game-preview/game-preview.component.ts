import {Component, computed, inject, input, Signal} from '@angular/core'
import {BetGame} from '../../../../model/game/bet-game'
import {DatePipe, NgIf, NgOptimizedImage} from '@angular/common'
import {ButtonComponent} from '../../button/button.component'
import {Router} from '@angular/router'

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

  game = input.required<BetGame>()

  openBet: Signal<boolean> = computed(() => !this.game().bet)

  focusGame(): void {
    const id = this.game().id
    const groupId = this.game().groupId
    this.router.navigateByUrl(`/bets/${groupId}/${id}`).then()
  }
}
