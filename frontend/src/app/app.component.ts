import {Component} from '@angular/core'
import {RouterOutlet} from '@angular/router'
import {HeaderComponent} from './shared/material-api'
import {User} from './model/user/user'
import {ScoreFormFieldComponent} from './shared/components/score-form-field/score-form-field.component'

@Component({
  selector: 'wm-root',
  standalone: true,
  imports: [RouterOutlet, HeaderComponent, ScoreFormFieldComponent],
  templateUrl: './app.component.html',
  styleUrl: './app.component.css'
})
export class AppComponent {
  mockUser: User = {
    id: 0,
    username: 'NoOneHardy',
    email: 'silas.hardegger@outlook.com',
    firstname: 'Silas',
    lastname: 'Hardegger',
    createdAt: new Date(),
    isActive: true,
    points: 0,
    updatedAt: new Date(),
    confirmedAt: new Date()
  }
}
