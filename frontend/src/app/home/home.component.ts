import {Component} from '@angular/core'
import {NgOptimizedImage} from '@angular/common'
import {RouterLink} from '@angular/router'
import {MatRipple} from '@angular/material/core'
import {InfoCardComponent} from './components/info-card/info-card.component'
import {PayoutBoardComponent} from './components/payout-board/payout-board.component'

@Component({
  selector: 'wm-home',
  standalone: true,
  imports: [
    NgOptimizedImage,
    RouterLink,
    MatRipple,
    InfoCardComponent,
    PayoutBoardComponent
  ],
  templateUrl: './home.component.html',
  styleUrl: './home.component.css'
})
export class HomeComponent {

}
