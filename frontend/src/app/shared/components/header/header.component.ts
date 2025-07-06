import {Component, HostListener, inject} from '@angular/core'
import {NavItemComponent} from './components/nav-item/nav-item.component'
import {UserMenuComponent} from './components/user-menu/user-menu.component'
import {RouterLink} from '@angular/router'
import {UserButtonComponent} from './components/user-button/user-button.component'
import {Store} from '@ngrx/store'
import {selectIsAdmin, selectUser} from '../../../user-management/store/user.feature'
import {deselectGroup} from '../../store/tournament.actions'
import {NgIf} from '@angular/common'

@Component({
  selector: 'wm-header',
  standalone: true,
  imports: [
    NavItemComponent,
    UserMenuComponent,
    RouterLink,
    UserButtonComponent,
    NgIf
  ],
  templateUrl: './header.component.html',
  styleUrl: './header.component.css'
})
export class HeaderComponent {
  private store = inject(Store)

  user = this.store.selectSignal(selectUser)
  isAdmin = this.store.selectSignal(selectIsAdmin)

  isLVP = window.innerWidth > 992
  hasNotifications = false
  isExpanded = true

  @HostListener('window:resize')
  calculateIsLVP(): void {
    this.isLVP = window.innerWidth > 992
  }

  deselectGroup(): void {
    this.store.dispatch(deselectGroup())
  }

  toggleMenu(): void {
    this.isExpanded = !this.isExpanded
  }

  closeMenuAfterTimeout(): void {
    setTimeout(() => {
      this.isExpanded = false
    }, 50)
  }
}
