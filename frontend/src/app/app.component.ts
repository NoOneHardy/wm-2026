import {Component} from '@angular/core'
import {RouterOutlet} from '@angular/router'
import {FormFieldComponent, HeaderComponent} from './shared/material-api'
import {User} from './model/user/user'

@Component({
  selector: 'wm-root',
  standalone: true,
  imports: [RouterOutlet, HeaderComponent, FormFieldComponent],
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
