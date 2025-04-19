import {Component, inject, OnInit} from '@angular/core'
import {RouterOutlet} from '@angular/router'
import {HeaderComponent} from './shared/material-api'
import {
  SnackbarDisplayComponent
} from './shared/components/snackbar/components/snackbar-display/snackbar-display.component'
import {Store} from '@ngrx/store'
import {fetchUserInfo} from './user-management/store/user.actions'
import {OverviewComponent} from './shared/components/overview/overview.component'
import {GroupViewComponent} from './shared/components/group-view/group-view.component'

@Component({
  selector: 'wm-root',
  standalone: true,
  imports: [RouterOutlet, HeaderComponent, SnackbarDisplayComponent, OverviewComponent, GroupViewComponent],
  templateUrl: './app.component.html',
  styleUrl: './app.component.css'
})
export class AppComponent implements OnInit {
  private store = inject(Store)

  ngOnInit(): void {
    this.store.dispatch(fetchUserInfo())
  }
}
