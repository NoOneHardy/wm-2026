import { Component } from '@angular/core'
import {NgOptimizedImage} from '@angular/common'
import {RouterLink} from '@angular/router'
import {MatRipple} from '@angular/material/core'

@Component({
  selector: 'wm-home',
  standalone: true,
  imports: [
    NgOptimizedImage,
    RouterLink,
    MatRipple
  ],
  templateUrl: './home.component.html',
  styleUrl: './home.component.css'
})
export class HomeComponent {

}
