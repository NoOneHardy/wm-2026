import {Component, computed, inject, OnInit} from '@angular/core'
import {Store} from '@ngrx/store'
import {InfoCardComponent} from './components/info-card/info-card.component'
import {PayoutBoardComponent} from './components/payout-board/payout-board.component'
import {HeroSectionComponent} from './components/hero-section/hero-section.component'
import {loadHomeData} from './store/home.actions'
import {selectHomeData} from './store/home.feature'

@Component({
  selector: 'wm-home',
  standalone: true,
  imports: [
    HeroSectionComponent,
    InfoCardComponent,
    PayoutBoardComponent
  ],
  templateUrl: './home.component.html',
  styleUrl: './home.component.css'
})
export class HomeComponent implements OnInit {
  private store = inject(Store)

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
