import {Component} from '@angular/core'
import {RouterLink} from '@angular/router'
import {MatRipple} from '@angular/material/core'
import {StadiumComponent} from '../stadium/stadium.component'

@Component({
  selector: 'wm-hero-section',
  standalone: true,
  imports: [
    RouterLink,
    MatRipple,
    StadiumComponent
  ],
  templateUrl: './hero-section.component.html',
  styleUrl: './hero-section.component.css'
})
export class HeroSectionComponent {

}
