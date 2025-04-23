import {inject, Injectable} from '@angular/core'
import {Actions, createEffect, ofType} from '@ngrx/effects'
import {GroupService} from '../services/group/group.service'
import {exhaustMap, map} from 'rxjs'
import {
  betsSaved,
  getOverviewGroups,
  groupSelected,
  overviewGroupsLoaded,
  saveBets,
  selectGroup
} from './tournament.actions'

// noinspection JSUnusedGlobalSymbols
@Injectable({
  providedIn: 'root'
})
export class TournamentEffects {
  private actions$ = inject(Actions)
  private groupService = inject(GroupService)

  getOverviewGroups = createEffect(() => this.actions$.pipe(
    ofType(getOverviewGroups),
    exhaustMap(() => {
      return this.groupService.getGroups().pipe(map(groups => {
        return overviewGroupsLoaded({groups})
      }))
    })
  ))

  selectGroup = createEffect(() => this.actions$.pipe(
    ofType(selectGroup),
    exhaustMap(action => {
      return this.groupService.getGroup(action.groupId).pipe(map(group => {
        return groupSelected({group})
      }))
    })
  ))

  saveBets = createEffect(() => this.actions$.pipe(
    ofType(saveBets),
    exhaustMap(action => {
      return this.groupService.saveBets(action.groupId, action.bets).pipe(map(group => {
        return betsSaved({group})
      }))
    })
  ))
}
