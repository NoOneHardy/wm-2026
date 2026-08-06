import {Component, input, ChangeDetectionStrategy} from '@angular/core'
import {SnackbarMessage} from './model/snackbar-message'

@Component({
  selector: 'bet-snackbar',
  imports: [],
  templateUrl: './snackbar.component.html',
  changeDetection: ChangeDetectionStrategy.Eager,
  styleUrl: './snackbar.component.css'
})
export class SnackbarComponent {
  message = input.required<SnackbarMessage, SnackbarMessage>({
    transform: v => ({
      ...v,
      type: v.type ?? 'success'
    })
  })
}
