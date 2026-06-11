import {Component, input} from '@angular/core'

@Component({
  selector: 'wm-stat-card',
  imports: [],
  templateUrl: './stat-card.component.html',
  styleUrl: './stat-card.component.css'
})
export class StatCardComponent {
  icon = input.required<string>()
  label = input.required<string>()
}
