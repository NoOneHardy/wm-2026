import {Component, input} from '@angular/core'

@Component({
  selector: 'wm-remaining-joker-display',
  standalone: true,
  imports: [],
  templateUrl: './remaining-joker-display.component.html',
  styleUrl: './remaining-joker-display.component.css'
})
export class RemainingJokerDisplayComponent {
  jokersRemaining = input.required<number>()
  jokersMax = input.required<number>()
  jokerType = input.required<number>()


  get jokerArray(): number[] {
    return Array(this.jokerType()).map((_, i) => i)
  }
}
