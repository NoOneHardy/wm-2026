import {Component, computed, ElementRef, inject, OnInit} from '@angular/core'
import {NavItemComponent} from './components/nav-item/nav-item.component'
import {UserMenuComponent} from './components/user-menu/user-menu.component'
import {EventType, Router, RouterLink} from '@angular/router'
import {UserButtonComponent} from './components/user-button/user-button.component'
import {Store} from '@ngrx/store'
import {selectIsAdmin, selectUser} from '../../../user-management/store/user.feature'

import {logout} from '../../../user-management/store/user.actions'
import {ViewportService} from '../../services/viewport/viewport.service'
import {animate, state, style, transition, trigger} from '@angular/animations'
import {NotificationsComponent} from './components/notifications/notifications.component'

@Component({
  selector: 'wm-header',
  host: {
    '(document:click)': 'closeMenuOnBlur($event)',
    '(document:keydown.escape)': 'closeMenu()'
  },
  imports: [
    NavItemComponent,
    UserMenuComponent,
    RouterLink,
    UserButtonComponent,
    NotificationsComponent
  ],
  templateUrl: './header.component.html',
  styleUrl: './header.component.css',
  animations: [
    trigger('expandable', [
      state('collapsed', style({
        'height': '0px',
        'padding-top': '0px',
        'padding-bottom': '0px',
        'opacity': 0
      })),
      state('expanded', style({
        'height': '*',
        'padding-top': '*',
        'padding-bottom': '*',
        'opacity': 1
      })),
      transition('collapsed <=> expanded', [
        animate(200)
      ])
    ])
  ]
})
export class HeaderComponent implements OnInit {
  private store = inject(Store)
  private el = inject(ElementRef)
  private viewportService = inject(ViewportService)
  private router = inject(Router)

  user = this.store.selectSignal(selectUser)
  isAdmin = this.store.selectSignal(selectIsAdmin)

  isVpL = computed(() => !this.viewportService.isVPMorSmaller())
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
    this.closeMenu()
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
