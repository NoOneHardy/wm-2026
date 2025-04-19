import {Component, inject, input, Signal} from '@angular/core'
import {Store} from '@ngrx/store'
import {selectActiveGroup, selectIsTournamentLoading} from '../../store/tournament.feature'
import {Mode} from '../../../model/mode'
import {Group} from '../../../model/group/group'
import {SpinnerComponent} from '../spinner/spinner.component'

@Component({
  selector: 'wm-group-view',
  standalone: true,
  imports: [
    SpinnerComponent
  ],
  templateUrl: './group-view.component.html',
  styleUrl: './group-view.component.css'
})
export class GroupViewComponent {
  private store = inject(Store)

  group: Signal<Group | null> = this.store.selectSignal(selectActiveGroup)
  isLoading = this.store.selectSignal(selectIsTournamentLoading)

  mode = input<Mode>('bet', {alias: 'mode'})
}
