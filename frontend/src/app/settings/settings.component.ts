import {Component, inject, ChangeDetectionStrategy} from '@angular/core'
import {SideNavComponent} from '../shared/components/side-nav/side-nav.component'
import {SideNavItemComponent} from '../shared/components/side-nav-item/side-nav-item.component'
import {Store} from '@ngrx/store'
import {logout} from '../user-management/store/user.actions'
import {RouterOutlet} from '@angular/router'

@Component({
  selector: 'bet-settings',
  imports: [
    SideNavComponent,
    SideNavItemComponent,
    RouterOutlet
  ],
  templateUrl: './settings.component.html',
  changeDetection: ChangeDetectionStrategy.Eager,
  styleUrl: './settings.component.css'
})
export default class SettingsComponent {
  private store = inject(Store)

  logout(): void {
    this.store.dispatch(logout())
  }
}
