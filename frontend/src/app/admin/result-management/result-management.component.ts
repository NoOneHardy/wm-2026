import {Component, inject} from '@angular/core'
import {Store} from '@ngrx/store'
import {hasActiveGroup} from '../../shared/store/tournament.feature'
import {GroupViewComponent} from '../../shared/components/group-view/group-view.component'
import {OverviewComponent} from '../../shared/components/overview/overview.component'

@Component({
  selector: 'wm-result-management',
  standalone: true,
  imports: [
    GroupViewComponent,
    OverviewComponent
  ],
  templateUrl: './result-management.component.html'
})
export class ResultManagementComponent {
  private store = inject(Store)

  hasGroupSelected = this.store.selectSignal(hasActiveGroup)
}
