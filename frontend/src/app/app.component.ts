import {Component, inject, OnInit} from '@angular/core'
import {RouterOutlet} from '@angular/router'
import {HeaderComponent} from './shared/material-api'
import {
  SnackbarDisplayComponent
} from './shared/components/snackbar/components/snackbar-display/snackbar-display.component'
import {Store} from '@ngrx/store'
import {fetchUserInfo} from './user-management/store/user.actions'
import {PositionComponent} from './shared/components/position/position.component'
import {Ranking} from './model/leaderboard/ranking'

@Component({
  selector: 'wm-root',
  standalone: true,
  imports: [RouterOutlet, HeaderComponent, SnackbarDisplayComponent, PositionComponent],
  templateUrl: './app.component.html',
  styleUrl: './app.component.css'
})
export class AppComponent implements OnInit {
  private store = inject(Store)

  ngOnInit(): void {
    this.store.dispatch(fetchUserInfo())
  }

  mockPosition: Ranking = {
    avatar: 'https://avatars.githubusercontent.com/u/116167986?v=4',
    username: 'No1Hardy',
    points: 100,
    previousRanking: 2,
    ranking: 1
  }
}
