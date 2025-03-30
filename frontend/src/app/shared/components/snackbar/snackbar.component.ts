import {Component, input} from '@angular/core'
import {SnackbarMessage} from './model/snackbar-message'

@Component({
  selector: 'wm-snackbar',
  standalone: true,
  imports: [],
  templateUrl: './snackbar.component.html',
  styleUrl: './snackbar.component.css'
})
export class SnackbarComponent {
  message = input.required<SnackbarMessage>()
}
