import {Component, inject, input, OnInit, Signal} from '@angular/core'
import {Store} from '@ngrx/store'
import {selectActiveGroup, selectIsTournamentLoading} from '../../store/tournament.feature'
import {Mode} from '../../../model/mode'
import {Group} from '../../../model/group/group'
import {selectGroup} from '../../store/tournament.actions'
import {DatePipe, DecimalPipe, NgForOf, NgIf} from '@angular/common'

@Component({
  selector: 'wm-group-view',
  standalone: true,
  imports: [
    DecimalPipe,
    NgIf,
    DatePipe,
    NgForOf
  ],
  templateUrl: './group-view.component.html',
  styleUrl: './group-view.component.css'
})
export class GroupViewComponent implements OnInit {
  private store = inject(Store)

  group: Signal<Group | null> = this.store.selectSignal(selectActiveGroup)
  isLoading = this.store.selectSignal(selectIsTournamentLoading)

  mode = input<Mode>('bet', {alias: 'mode'})

  ngOnInit() {
    // TODO: remove
    this.store.dispatch(selectGroup({groupId: 'ab774ee8-c95e-48d2-808e-8962df3cd132'}))
  }
}
