import { Component } from '@angular/core'
import {SpinnerComponent} from '../shared/components/spinner/spinner.component'

@Component({
  selector: 'wm-email-verification',
  standalone: true,
  imports: [
    SpinnerComponent
  ],
  templateUrl: './email-verification.component.html',
  styleUrl: './email-verification.component.css'
})
export class EmailVerificationComponent {

}
