import {Component, inject, OnInit} from '@angular/core'
import {RouterOutlet} from '@angular/router'
import {HeaderComponent} from './shared/material-api'
import {
  SnackbarDisplayComponent
} from './shared/components/snackbar/components/snackbar-display/snackbar-display.component'
import {Store} from '@ngrx/store'
import {fetchUserInfo} from './user-management/store/user.actions'
import {BannerDisplayComponent} from './shared/components/banner-display/banner-display.component'

@Component({
  selector: 'bet-root',
  imports: [RouterOutlet, HeaderComponent, SnackbarDisplayComponent, BannerDisplayComponent],
  templateUrl: './app.component.html',
  styleUrl: './app.component.css'
})
export class AppComponent implements OnInit {
  private store = inject(Store)

  ngOnInit(): void {
    this.store.dispatch(fetchUserInfo())
  }
}
