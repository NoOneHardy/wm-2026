import {Component} from '@angular/core'
import {MatSidenav, MatSidenavContainer, MatSidenavContent} from '@angular/material/sidenav'
import {RouterOutlet} from '@angular/router'
import {SideNavComponent} from '../shared/components/side-nav/side-nav.component'
import {SideNavItemComponent} from '../side-nav-item/side-nav-item.component'

@Component({
  selector: 'wm-settings',
  standalone: true,
  imports: [
    MatSidenavContainer,
    MatSidenav,
    MatSidenavContent,
    RouterOutlet,
    SideNavComponent,
    SideNavItemComponent
  ],
  templateUrl: './settings.component.html',
  styleUrl: './settings.component.css'
})
export class SettingsComponent {

}
