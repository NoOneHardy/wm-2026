import {Component, input, ChangeDetectionStrategy} from '@angular/core'

@Component({
  selector: 'bet-info-card',
  imports: [],
  templateUrl: './info-card.component.html',
  styleUrl: './info-card.component.css',
  changeDetection: ChangeDetectionStrategy.Eager,
  host: {
    '[class.-accent]': 'accent()'
  }
})
export class InfoCardComponent {
  icon = input.required<string>()
  name = input.required<string>()
  accent = input<boolean, boolean | ''>(false, {
    transform: (v) => v === '' || v
  })
}
