import {Component, computed, inject, OnInit, Signal} from '@angular/core'
import {Store} from '@ngrx/store'
import {getLeaderboard} from '../shared/store/tournament.actions'
import {selectLeaderboard} from '../shared/store/tournament.feature'
import {selectUser} from '../user-management/store/user.feature'
import {Ranking} from '../model/leaderboard/ranking'
import {PositionComponent} from '../shared/components/position/position.component'
import {NgForOf, NgIf, NgOptimizedImage} from '@angular/common'

@Component({
  selector: 'wm-leaderboard',
  standalone: true,
  imports: [
    PositionComponent,
    NgForOf,
    NgOptimizedImage,
    NgIf
  ],
  templateUrl: './leaderboard.component.html',
  styleUrl: './leaderboard.component.css'
})
export class LeaderboardComponent implements OnInit {
  private store = inject(Store)

  leaderboard = this.store.selectSignal(selectLeaderboard)
  user = this.store.selectSignal(selectUser)

  userPos: Signal<Ranking | null> = computed(() => {
    const leaderboard = this.leaderboard()
    const user = this.user()
    if (!user) return null
    return leaderboard.find(r => r.id === user.id) ?? null
  })

  ngOnInit(): void {
    this.store.dispatch(getLeaderboard())
  }
}
