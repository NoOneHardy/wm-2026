import {Component, input, output} from '@angular/core'

@Component({
  selector: 'wm-side-nav-item',
  standalone: true,
  imports: [],
  templateUrl: './side-nav-item.component.html',
  styleUrl: './side-nav-item.component.css'
})
export class SideNavItemComponent {
  route = input<string | null>(null)
  icon = input<string | null>(null)
  clickAction = output<Event>()
}
