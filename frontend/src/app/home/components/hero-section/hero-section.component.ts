import {Component, ChangeDetectionStrategy} from '@angular/core'
import {RouterLink} from '@angular/router'
import {MatRipple} from '@angular/material/core'
import {StadiumComponent} from '../stadium/stadium.component'

@Component({
  selector: 'bet-hero-section',
  imports: [
    RouterLink,
    MatRipple,
    StadiumComponent
  ],
  templateUrl: './hero-section.component.html',
  changeDetection: ChangeDetectionStrategy.Eager,
  styleUrl: './hero-section.component.css'
})
export class HeroSectionComponent {

}
