import {Component, input} from '@angular/core'
import {Ranking} from '../../../model/leaderboard/ranking'
import {NgIf, NgOptimizedImage} from '@angular/common'

@Component({
  selector: 'wm-position',
  standalone: true,
  imports: [
    NgIf,
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
}
