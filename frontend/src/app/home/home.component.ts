import {Component, computed, inject, OnInit, ChangeDetectionStrategy} from '@angular/core'
import {Store} from '@ngrx/store'
import {InfoCardComponent} from './components/info-card/info-card.component'
import {PayoutBoardComponent} from './components/payout-board/payout-board.component'
import {HeroSectionComponent} from './components/hero-section/hero-section.component'
import {loadHomeData} from './store/home.actions'
import {selectHomeData, selectIsHomeLoading} from './store/home.feature'
import {SpinnerComponent} from '../shared/components/spinner/spinner.component'

@Component({
  selector: 'bet-home',
  imports: [
    HeroSectionComponent,
    InfoCardComponent,
    PayoutBoardComponent,
    SpinnerComponent
  ],
  templateUrl: './home.component.html',
  changeDetection: ChangeDetectionStrategy.Eager,
  styleUrl: './home.component.css'
})
export class HomeComponent implements OnInit {
  private store = inject(Store)

  isLoading = this.store.selectSignal(selectIsHomeLoading)
  homeData = this.store.selectSignal(selectHomeData)
  stats = computed(() => {
    const data = this.homeData()
    return {
      jackpot: data ? `CHF ${data.jackpot}` : 'CHF --',
      players: data ? `${data.players}` : '--',
      games: data ? `${data.games}` : '--'
    }
  })

  ngOnInit(): void {
    this.store.dispatch(loadHomeData())
  }
}
