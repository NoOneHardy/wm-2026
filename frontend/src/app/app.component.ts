import {Component, inject, OnInit} from '@angular/core'
import {RouterOutlet} from '@angular/router'
import {HeaderComponent} from './shared/material-api'
import {
  SnackbarDisplayComponent
} from './shared/components/snackbar/components/snackbar-display/snackbar-display.component'
import {Store} from '@ngrx/store'
import {fetchUserInfo} from './user-management/store/user.actions'
import {BannerDisplayComponent} from './shared/components/banner-display/banner-display.component'
import {AppearanceService} from './shared/services/appearance/appearance.service'

@Component({
  selector: 'wm-root',
  standalone: true,
  imports: [RouterOutlet, HeaderComponent, SnackbarDisplayComponent, BannerDisplayComponent],
  templateUrl: './app.component.html',
  styleUrl: './app.component.css'
})
export class AppComponent implements OnInit {
  private store = inject(Store)
  private appearanceService = inject(AppearanceService)

  ngOnInit(): void {
    this.appearanceService.initialize()
    this.store.dispatch(fetchUserInfo())
  }
}
