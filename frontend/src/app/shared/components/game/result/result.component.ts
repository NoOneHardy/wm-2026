import {Component, HostBinding, input} from '@angular/core'
import {Score} from '../../../../model/game/score'

@Component({
  selector: 'bet-result',
  standalone: true,
  templateUrl: './result.component.html',
  styleUrl: './result.component.css'
})
export class ResultComponent {
  result = input<Score | null>(null)

  large = input<boolean, boolean | ''>(false, {
    transform: (v) => v === '' || v
  })

  @HostBinding('class.-large')
  get largeClass(): boolean {
    return this.large()
  }
}
