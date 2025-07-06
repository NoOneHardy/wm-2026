import {Component, computed, ElementRef, inject, OnInit} from '@angular/core'
import {NavItemComponent} from './components/nav-item/nav-item.component'
import {UserMenuComponent} from './components/user-menu/user-menu.component'
import {EventType, Router, RouterLink} from '@angular/router'
import {UserButtonComponent} from './components/user-button/user-button.component'
import {Store} from '@ngrx/store'
import {selectIsAdmin, selectUser} from '../../../user-management/store/user.feature'
import {deselectGroup} from '../../store/tournament.actions'
import {NgIf} from '@angular/common'
import {logout} from '../../../user-management/store/user.actions'
import {ViewportService} from '../../services/viewport/viewport.service'

@Component({
  selector: 'wm-header',
  standalone: true,
  host: {
    '(document:click)': 'closeMenuOnBlur($event)'
  },
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
export class HeaderComponent implements OnInit {
  private store = inject(Store)
  private el = inject(ElementRef)
  private viewportService = inject(ViewportService)
  private router = inject(Router)

  user = this.store.selectSignal(selectUser)
  isAdmin = this.store.selectSignal(selectIsAdmin)

  isVpL = computed(() => !this.viewportService.isVPMorSmaller())

  hasNotifications = false
  isExpanded = false

  ngOnInit(): void {
    this.router.events.subscribe(($event) => {
      if ($event.type === EventType.NavigationEnd) {
        this.closeMenu()
      }
    })
  }

  logout(): void {
    this.store.dispatch(logout())
  }

  deselectGroup(): void {
    this.store.dispatch(deselectGroup())
  }

  toggleMenu(): void {
    this.isExpanded = !this.isExpanded
  }

  closeMenuOnBlur(e: MouseEvent): void {
    if (!this.el.nativeElement.contains(e.target)) {
      this.isExpanded = false
    }
  }

  closeMenu(): void {
      this.isExpanded = false
  }
}
