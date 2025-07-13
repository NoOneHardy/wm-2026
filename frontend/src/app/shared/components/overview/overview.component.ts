import {Component, computed, effect, inject, input, OnInit} from '@angular/core'
import {DecimalPipe, NgForOf, NgIf, NgOptimizedImage} from '@angular/common'
import {ButtonComponent} from '../button/button.component'
import {Store} from '@ngrx/store'
import {selectGroups, selectIsTournamentLoading} from '../../store/tournament.feature'
import {getOverviewGroups, selectGroup} from '../../store/tournament.actions'
import {SpinnerComponent} from '../spinner/spinner.component'
import {Mode} from '../../../model/mode'
import {ActivatedRoute} from '@angular/router'

@Component({
  selector: 'wm-overview',
  standalone: true,
  imports: [
    NgForOf,
    NgOptimizedImage,
    DecimalPipe,
    ButtonComponent,
    NgIf,
    SpinnerComponent
  ],
  templateUrl: './overview.component.html',
  styleUrl: './overview.component.css'
})
export class OverviewComponent implements OnInit {
  private store = inject(Store)
  private activatedRoute = inject(ActivatedRoute)

  groups = this.store.selectSignal(selectGroups)
  isLoading = this.store.selectSignal(selectIsTournamentLoading)

  mode = input<Mode>('bet', {alias: 'mode'})
  totalPercentage = computed(() => {
    const groups = this.groups()

    const total = groups.length
    if (total === 0) return 0

    if (this.mode() == 'bet') {
      const totalBets = groups.reduce((acc, group) => acc + group.percentage, 0)
      return totalBets / total
    } else {
      const totalResults = groups.reduce((acc, group) => acc + group.percentageResult, 0)
      return totalResults / total
    }
  })

  constructor() {
    effect(() => {
      const isLoading = this.isLoading()
      if (isLoading) return

      const groupId = this.activatedRoute.snapshot.queryParamMap.get('group')
      if (groupId) {
        this.store.dispatch(selectGroup({groupId}))
      }
    })
  }

  ngOnInit(): void {
    this.store.dispatch(getOverviewGroups())
  }

  defaultGroups = computed(() => {
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
    this.store.dispatch(selectGroup({groupId}))
  }
}
