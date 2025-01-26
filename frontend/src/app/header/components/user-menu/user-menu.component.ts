import {Component, input} from '@angular/core'
import {User} from '../../../model/user/user'

@Component({
  selector: 'wm-user-menu',
  standalone: true,
  imports: [],
  templateUrl: './user-menu.component.html',
  styleUrl: './user-menu.component.css'
})
export class UserMenuComponent {
  user = input<User | null>()
}
