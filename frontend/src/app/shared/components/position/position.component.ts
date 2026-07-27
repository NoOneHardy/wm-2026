import {Component, computed, input} from '@angular/core'
import {Ranking} from '../../../model/leaderboard/ranking'
import { NgOptimizedImage } from '@angular/common'

@Component({
  selector: 'bet-position',
  imports: [
    NgOptimizedImage
  ],
  templateUrl: './position.component.html',
  styleUrl: './position.component.css'
})
export class PositionComponent {
  ranking = input.required<Ranking>()
  highlight = input<boolean, boolean | ''>(false, {
    transform: v => v === '' || v
  })

  movementIcon = computed(() => {
    const ranking = this.ranking()
    const movement = ranking.prevRanking - ranking.ranking

    if (movement >= 5) return 'keyboard_double_arrow_up'
    if (movement <= -5) return 'keyboard_double_arrow_down'
    if (movement > 0) return 'keyboard_arrow_up'
    if (movement < 0) return 'keyboard_arrow_down'
    return 'equal'
  })
}
