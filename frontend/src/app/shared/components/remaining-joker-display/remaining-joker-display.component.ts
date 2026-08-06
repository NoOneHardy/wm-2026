import {Component, input, ChangeDetectionStrategy} from '@angular/core'

@Component({
  selector: 'bet-remaining-joker-display',
  imports: [],
  templateUrl: './remaining-joker-display.component.html',
  changeDetection: ChangeDetectionStrategy.Eager,
  styleUrl: './remaining-joker-display.component.css'
})
export class RemainingJokerDisplayComponent {
  jokersRemaining = input.required<number>()
  jokersMax = input.required<number>()
  jokerMultiplier = input.required<number>()

  get jokerArray(): number[] {
    return Array.from({length: this.jokerMultiplier()}, (_, i) => i)
  }
}
