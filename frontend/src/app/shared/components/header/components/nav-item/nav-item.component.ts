import {Component, input, output} from '@angular/core'
import {NgIf} from '@angular/common'
import {RouterLink, RouterLinkActive} from '@angular/router'

@Component({
  selector: 'wm-nav-item',
  standalone: true,
  imports: [
    NgIf,
    RouterLink,
    RouterLinkActive
  ],
  templateUrl: './nav-item.component.html',
  styleUrl: './nav-item.component.css'
})
export class NavItemComponent {
  icon = input<string | null>(null)
  displayName = input<string | null>(null)
  route = input<string>('/')
  exact = input<boolean, boolean | ''>(false, {
    transform: v => v === '' || v
  })
  clickEvent = output<void>()
}
