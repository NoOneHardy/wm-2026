import {Component, computed, inject, input, OnInit} from '@angular/core'
import {DecimalPipe, NgForOf, NgOptimizedImage} from '@angular/common'
import {ButtonComponent} from '../button/button.component'
import {Store} from '@ngrx/store'
import {selectGroups} from '../../store/tournament.feature'
import {getOverviewGroups} from '../../store/tournament.actions'

@Component({
  selector: 'wm-overview',
  standalone: true,
  imports: [
    NgForOf,
    NgOptimizedImage,
    DecimalPipe,
    ButtonComponent
  ],
  templateUrl: './overview.component.html',
  styleUrl: './overview.component.css'
})
export class OverviewComponent implements OnInit {
  private store = inject(Store)

  groups = this.store.selectSignal(selectGroups)

  mode = input<'bet' | 'result' | 'admin'>('bet')
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
    })
  })

  knockoutGroups = computed(() => {
    const groups = this.groups()
    return groups.filter(group => group.isKnockout).map(group => {
      return {
        ...group,
        percentage: this.mode() === 'bet' ? group.percentage : group.percentageResult
      }
    })
  })
}
