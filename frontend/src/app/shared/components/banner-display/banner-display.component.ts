import {Component, inject, Signal, ChangeDetectionStrategy} from '@angular/core'
import {Store} from '@ngrx/store'
import {selectUser} from '../../../user-management/store/user.feature'
import {User} from '../../../model/user/user'
import {sendEmailVerificationLink} from '../../../user-management/store/user.actions'

@Component({
  selector: 'bet-banner-display',
  imports: [],
  templateUrl: './banner-display.component.html',
  changeDetection: ChangeDetectionStrategy.Eager,
  styleUrl: './banner-display.component.css'
})
export class BannerDisplayComponent {
  private store = inject(Store)

  user: Signal<User | null> = this.store.selectSignal(selectUser)

  sendEmailVerificationLink(): void {
    this.store.dispatch(sendEmailVerificationLink())
  }
}
