import {Component, input, output, ChangeDetectionStrategy} from '@angular/core'
import {RouterLink, RouterLinkActive} from '@angular/router'

@Component({
  selector: 'bet-side-nav-item',
  imports: [
    RouterLink,
    RouterLinkActive
  ],
  templateUrl: './side-nav-item.component.html',
  changeDetection: ChangeDetectionStrategy.Eager,
  styleUrl: './side-nav-item.component.css'
})
export class SideNavItemComponent {
  route = input<string | null>(null)
  icon = input<string | null>(null)
  clickAction = output<Event>()
}
