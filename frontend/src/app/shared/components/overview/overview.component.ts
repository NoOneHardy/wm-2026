import {Component, computed, input} from '@angular/core'
import {CardGroup} from '../../../model/group/card-group'
import {DecimalPipe, NgForOf, NgOptimizedImage} from '@angular/common'
import {ButtonComponent} from '../button/button.component'

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
export class OverviewComponent {
  mode = input<'bet' | 'result' | 'admin'>('bet')
  groups = input<CardGroup[]>([])
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
