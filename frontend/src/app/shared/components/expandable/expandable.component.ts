import {Component, input} from '@angular/core'
import {animate, state, style, transition, trigger} from '@angular/animations'

@Component({
  selector: 'wm-expandable',
  standalone: true,
  templateUrl: './expandable.component.html',
  styleUrl: './expandable.component.css',
  animations: [
    trigger('expandable', [
      state('collapsed', style({
        'height': '0px',
        'padding-top': '0px'
      })),
      state('expanded', style({
        'height': '*',
        'padding-top': '*'
      })),
      transition('collapsed <=> expanded', [
        animate(200)
      ])
    ])
  ]
})
export class ExpandableComponent {
  name = input.required<string>()

  expanded = false
}
