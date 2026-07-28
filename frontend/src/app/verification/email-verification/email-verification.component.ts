import {Component, inject, OnInit, ChangeDetectionStrategy} from '@angular/core'
import {SpinnerComponent} from '../../shared/components/spinner/spinner.component'
import {Store} from '@ngrx/store'
import {verifyEmail} from '../../user-management/store/user.actions'
import {ActivatedRoute} from '@angular/router'

@Component({
  selector: 'bet-email-verification',
  imports: [
    SpinnerComponent
  ],
  templateUrl: './email-verification.component.html',
  changeDetection: ChangeDetectionStrategy.Eager,
  styleUrl: './email-verification.component.css'
})
export default class EmailVerificationComponent implements OnInit {
  private store = inject(Store)
  private activatedRoute = inject(ActivatedRoute)

  ngOnInit(): void {
    const urlParams = this.activatedRoute.snapshot.queryParamMap
    const code = urlParams.get('code')
    if (code) {
      this.store.dispatch(verifyEmail({code}))
    }
  }
}
