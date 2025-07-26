import {Component, HostBinding, input} from '@angular/core'
import {DetailedPoints} from '../../../../model/game/detailed-points'

@Component({
  selector: 'wm-point-table',
  standalone: true,
  templateUrl: './point-table.component.html',
  styleUrl: './point-table.component.css'
})
export class PointTableComponent {
  points = input<DetailedPoints | null>(null)
  total = input.required<number>()
  large = input<boolean, boolean | ''>(false, {
    transform: (v) => v === '' || v
  })

  @HostBinding('class.-large')
  get largeClass(): boolean {
    return this.large()
  }

  get hasDetails(): boolean {
    const points = this.points()
    return !!points && Object.keys(points).length > 0
  }
}
