import {Component, computed, DestroyRef, inject, input, Signal, signal} from '@angular/core'
import {DatePipe, DecimalPipe, NgOptimizedImage} from '@angular/common'
import {Router} from '@angular/router'
import {BetGame} from '../../../model/game/bet-game'
import {ButtonComponent} from '../../../shared/components/button/button.component'

interface Countdown {
  days: number
  hours: number
  minutes: number
  seconds: number
}

@Component({
  selector: 'wm-next-kickoff',
  imports: [
    DatePipe,
    DecimalPipe,
    NgOptimizedImage,
    ButtonComponent
  ],
  templateUrl: './next-kickoff.component.html',
  styleUrl: './next-kickoff.component.css'
})
export class NextKickoffComponent {
  private router = inject(Router)
  private destroyRef = inject(DestroyRef)

  game = input<BetGame | null>(null)

  private now = signal(Date.now())

  constructor() {
    const interval = setInterval(() => this.now.set(Date.now()), 1000)
    this.destroyRef.onDestroy(() => clearInterval(interval))
  }

  countdown: Signal<Countdown | null> = computed(() => {
    const game = this.game()
    if (!game) return null

    const remaining = Math.floor((new Date(game.timestamp).valueOf() - this.now()) / 1000)
    if (remaining <= 0) return null

    return {
      days: Math.floor(remaining / 86400),
      hours: Math.floor(remaining / 3600) % 24,
      minutes: Math.floor(remaining / 60) % 60,
      seconds: remaining % 60
    }
  })

  jokerStars: Signal<number[]> = computed(() => {
    const bet = this.game()?.bet
    if (!bet || bet.joker === 1) return []
    return Array.from({length: bet.joker}, (_, i) => i + 1)
  })

  openGame(): void {
    const game = this.game()
    if (!game) return
    this.router.navigateByUrl(`/bets/${game.groupId}/${game.id}`).then()
  }
}
