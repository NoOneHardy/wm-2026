import {Component, inject, OnInit} from '@angular/core'
import {SpinnerComponent} from '../shared/components/spinner/spinner.component'
import {Store} from '@ngrx/store'
import {verifyEmail} from '../user-management/store/user.actions'

@Component({
  selector: 'wm-email-verification',
  standalone: true,
  imports: [
    SpinnerComponent
  ],
  templateUrl: './email-verification.component.html',
  styleUrl: './email-verification.component.css'
})
export class EmailVerificationComponent implements OnInit {
  private store = inject(Store)

  ngOnInit(): void {
    const urlParams = new URLSearchParams(window.location.search)
    const code = urlParams.get('code')
    if (code) {
      this.store.dispatch(verifyEmail({code}))
    }
  }
}
