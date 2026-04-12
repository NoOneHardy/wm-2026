import {Store} from '@ngrx/store'
import {Component, inject, OnInit} from '@angular/core'
import {OverviewComponent} from '../shared/components/overview/overview.component'
import {GroupViewComponent} from '../shared/components/group-view/group-view.component'
import {ActivatedRoute} from '@angular/router'
import {loadGroup} from '../shared/store/tournament.actions'

@Component({
  selector: 'wm-bet-overview',
  imports: [
    OverviewComponent,
    GroupViewComponent
  ],
  templateUrl: './bet-management.component.html'
})
export default class BetManagementComponent implements OnInit {
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
