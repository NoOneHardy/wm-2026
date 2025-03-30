import {Component} from '@angular/core'
import {RouterOutlet} from '@angular/router'
import {HeaderComponent} from './shared/material-api'
import {
  SnackbarDisplayComponent
} from './shared/components/snackbar/components/snackbar-display/snackbar-display.component'
import {SnackbarService} from './shared/services/snackbar.service'

@Component({
  selector: 'wm-root',
  standalone: true,
  imports: [RouterOutlet, HeaderComponent, SnackbarDisplayComponent],
  templateUrl: './app.component.html',
  styleUrl: './app.component.css'
})
export class AppComponent {
  constructor(snackbarService: SnackbarService) {
    snackbarService.addMessage({
        message: 'User created successfully',
      })
    snackbarService.addMessage({
      message: 'Could not create user: Username is already taken',
      type: 'error',
      duration: 10000
    })
  }

}
