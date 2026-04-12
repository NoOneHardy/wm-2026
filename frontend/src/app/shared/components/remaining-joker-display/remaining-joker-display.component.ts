import {Component, input} from '@angular/core'

@Component({
  selector: 'wm-remaining-joker-display',
  imports: [],
  templateUrl: './remaining-joker-display.component.html',
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
