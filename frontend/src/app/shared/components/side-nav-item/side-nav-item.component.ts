import {Component, input, output} from '@angular/core'
import {RouterLink, RouterLinkActive} from '@angular/router'

@Component({
  selector: 'wm-side-nav-item',
  imports: [
    RouterLink,
    RouterLinkActive
  ],
  templateUrl: './side-nav-item.component.html',
  styleUrl: './side-nav-item.component.css'
})
export class SideNavItemComponent {
  route = input<string | null>(null)
  icon = input<string | null>(null)
  clickAction = output<Event>()
}
