import {Component, inject, OnInit, ChangeDetectionStrategy} from '@angular/core'
import {Store} from '@ngrx/store'
import {GroupViewComponent} from '../../shared/components/group-view/group-view.component'
import {OverviewComponent} from '../../shared/components/overview/overview.component'
import {ActivatedRoute} from '@angular/router'
import {loadGroup} from '../../shared/store/tournament.actions'

@Component({
  selector: 'bet-result-management',
  imports: [
    GroupViewComponent,
    OverviewComponent
  ],
  changeDetection: ChangeDetectionStrategy.Eager,
  templateUrl: './result-management.component.html'
})
export class ResultManagementComponent implements OnInit {
  private activatedRoute = inject(ActivatedRoute)
  private store = inject(Store)

  groupId: string | null = null
  gameId: string | null = null

  ngOnInit(): void {
    this.groupId = this.activatedRoute.snapshot.paramMap.get('groupId')
    this.gameId = this.activatedRoute.snapshot.paramMap.get('gameId')

    if (this.groupId) this.store.dispatch(loadGroup({groupId: this.groupId}))
  }
}
