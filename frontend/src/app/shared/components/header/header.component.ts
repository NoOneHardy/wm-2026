import {Component, input} from '@angular/core'
import {User} from '../../../model/user/user'
import {NavItemComponent} from './components/nav-item/nav-item.component'
import {UserMenuComponent} from './components/user-menu/user-menu.component'
import {RouterLink} from '@angular/router'
import {UserButtonComponent} from './components/user-button/user-button.component'

@Component({
  selector: 'wm-header',
  standalone: true,
  imports: [
    NavItemComponent,
    UserMenuComponent,
    RouterLink,
    UserButtonComponent
  ],
  templateUrl: './header.component.html',
  styleUrl: './header.component.css'
})
export class HeaderComponent {
  user = input<User | null>(null)
}
