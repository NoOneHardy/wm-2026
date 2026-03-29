import {Component} from '@angular/core'
import {InfoCardComponent} from './components/info-card/info-card.component'
import {PayoutBoardComponent} from './components/payout-board/payout-board.component'
import {HeroSectionComponent} from './components/hero-section/hero-section.component'

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
export class HomeComponent {

}
