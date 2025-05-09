import {Store} from '@ngrx/store'
import {Component, inject} from '@angular/core'
import {OverviewComponent} from '../shared/components/overview/overview.component'
import {GroupViewComponent} from '../shared/components/group-view/group-view.component'
import {hasActiveGroup} from '../shared/store/tournament.feature'

@Component({
  selector: 'wm-bet-overview',
  standalone: true,
  imports: [
    OverviewComponent,
    GroupViewComponent
  ],
  templateUrl: './bet-management.component.html'
})
export class BetManagementComponent {
  private store = inject(Store)

  hasGroupSelected = this.store.selectSignal(hasActiveGroup)
}
