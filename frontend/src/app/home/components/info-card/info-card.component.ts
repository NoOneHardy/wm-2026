import {Component, input} from '@angular/core'

@Component({
  selector: 'wm-info-card',
  standalone: true,
  imports: [],
  templateUrl: './info-card.component.html',
  styleUrl: './info-card.component.css',
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
