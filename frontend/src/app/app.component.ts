import { Component } from '@angular/core'
import { RouterOutlet } from '@angular/router'
import {HeaderComponent} from './header/header.component'

@Component({
  selector: 'wm-root',
  standalone: true,
  imports: [RouterOutlet, HeaderComponent],
  templateUrl: './app.component.html',
  styleUrl: './app.component.css'
})
export class AppComponent {
  title = 'frontend'
}
