import {Component, inject, Signal} from '@angular/core'

import {SnackbarMessage} from '../../model/snackbar-message'
import {SnackbarComponent} from '../../snackbar.component'
import {SnackbarService} from '../../../../services/snackbar/snackbar.service'

@Component({
  selector: 'wm-snackbar-display',
  imports: [
    SnackbarComponent
  ],
  templateUrl: './snackbar-display.component.html',
  styleUrl: './snackbar-display.component.css'
})
export class SnackbarDisplayComponent {
  private service = inject(SnackbarService)
  messages: Signal<SnackbarMessage[]> = this.service.messages
}
