import {Component, computed, inject, OnInit, Signal} from '@angular/core'
import {Store} from '@ngrx/store'
import {getLeaderboard} from '../shared/store/tournament.actions'
import {selectIsTournamentLoading, selectLeaderboard} from '../shared/store/tournament.feature'
import {selectUser} from '../user-management/store/user.feature'
import {Ranking} from '../model/leaderboard/ranking'
import {PositionComponent} from '../shared/components/position/position.component'
import {SpinnerComponent} from '../shared/components/spinner/spinner.component'

@Component({
  selector: 'wm-leaderboard',
  standalone: true,
  imports: [
    PositionComponent,
    SpinnerComponent
  ],
  templateUrl: './leaderboard.component.html',
  styleUrl: './leaderboard.component.css'
})
export class LeaderboardComponent implements OnInit {
  private store = inject(Store)

  leaderboard = this.store.selectSignal(selectLeaderboard)
  user = this.store.selectSignal(selectUser)
  isLoading = this.store.selectSignal(selectIsTournamentLoading)

  userPos: Signal<Ranking | null> = computed(() => {
    const leaderboard = this.leaderboard()
    const user = this.user()
    if (!user) return null
    return leaderboard.find(r => r.id === user.id) ?? null
  })

  ngOnInit(): void {
    this.store.dispatch(getLeaderboard())
  }

  get subHeading(): string {
    const userPos = this.userPos()
    if (userPos) {
      const movement = userPos.prevRanking - userPos.ranking
      if (movement > 0) {
        return `Gratuliere, du bist ${movement === 1 ? '1 Rang' : `${movement} Ränge`} aufgestiegen!`
      } else if (movement < 0) {
        return `Schade, du bist ${movement === -1 ? '1 Rang' : `${Math.abs(movement)} Ränge`} abgestiegen.`
      }
    }

    return 'Verschaffe dir eine Übersicht über das Spiel.'
  }
}
