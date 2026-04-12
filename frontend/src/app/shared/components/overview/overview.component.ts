import {Component, computed, effect, inject, input, OnInit, Signal} from '@angular/core'
import { DecimalPipe, NgOptimizedImage } from '@angular/common'
import {ButtonComponent} from '../button/button.component'
import {Store} from '@ngrx/store'
import {
  selectGroups,
  selectIsTournamentLoading,
  selectPercentage,
  selectPercentageResult
} from '../../store/tournament.feature'
import {getOverviewGroups, loadGroup} from '../../store/tournament.actions'
import {SpinnerComponent} from '../spinner/spinner.component'
import {Mode} from '../../../model/mode'
import {ActivatedRoute, Router} from '@angular/router'
import {CardGroup} from '../../../model/group/card-group'

@Component({
  selector: 'wm-overview',
  imports: [
    NgOptimizedImage,
    DecimalPipe,
    ButtonComponent,
    SpinnerComponent
  ],
  templateUrl: './overview.component.html',
  styleUrl: './overview.component.css'
})
export class OverviewComponent implements OnInit {
  private store = inject(Store)
  private router = inject(Router)
  private activatedRoute = inject(ActivatedRoute)

  mode = input<Mode>('bet', {alias: 'mode'})

  groups = this.store.selectSignal(selectGroups)
  totalPercentage = computed(() => {
    if (this.mode() === 'admin') return this.store.selectSignal(selectPercentageResult)()

    return this.store.selectSignal(selectPercentage)()
  })


  isLoading = this.store.selectSignal(selectIsTournamentLoading)

  constructor() {
    effect(() => {
      const isLoading = this.isLoading()
      if (isLoading) return

      const groupId = this.activatedRoute.snapshot.queryParamMap.get('group')
      if (groupId) {
        this.store.dispatch(loadGroup({groupId}))
      }
    })
  }

  ngOnInit(): void {
    this.store.dispatch(getOverviewGroups())
  }

  defaultGroups: Signal<CardGroup[]> = computed(() => {
    const groups = this.groups()
    return groups.filter(group => !group.isKnockout).map(group => {
      return {
        ...group,
        percentage: this.mode() === 'bet' ? group.percentage : group.percentageResult
      }
    }).sort((a, b) => a.name > b.name ? 1 : -1)
  })

  knockoutGroups = computed(() => {
    const groups = this.groups()
    return groups.filter(group => group.isKnockout).map(group => {
      return {
        ...group,
        percentage: this.mode() === 'bet' ? group.percentage : group.percentageResult
      }
    }).sort((a, b) => a.order - b.order)
  })

  selectGroup(groupId: string): void {
    this.router.navigate([this.mode() === 'admin' ? '/admin/results' : 'bets', groupId]).then()
    this.store.dispatch(loadGroup({groupId}))
  }
}
