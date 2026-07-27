import {Component, input, output} from '@angular/core'

import {RouterLink, RouterLinkActive} from '@angular/router'
import {MatRipple} from '@angular/material/core'

@Component({
  selector: 'bet-nav-item',
  imports: [
    RouterLink,
    RouterLinkActive,
    MatRipple
  ],
  templateUrl: './nav-item.component.html',
  styleUrl: './nav-item.component.css'
})
export class NavItemComponent {
  icon = input<string | null>(null)
  displayName = input<string | null>(null)
  route = input<string | null>('/')
  exact = input<boolean, boolean | ''>(false, {
    transform: v => v === '' || v
  })
  gradient = input<boolean, boolean | ''>(false, {
    transform: v => v === '' || v
  })
  clickEvent = output<void>()
}
