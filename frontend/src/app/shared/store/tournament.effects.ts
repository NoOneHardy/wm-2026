import {inject, Injectable} from '@angular/core'
import {Actions, createEffect, ofType} from '@ngrx/effects'
import {GroupService} from '../services/group/group.service'
import {catchError, exhaustMap, map, of, tap} from 'rxjs'
import {
  betsSaved,
  dashboardDataLoaded,
  getLeaderboard,
  getOverviewGroups,
  groupLoaded,
  leaderboardLoaded,
  loadDashboardData,
  overviewGroupsLoaded,
  resetSaving,
  resultsSaved,
  saveBets,
  saveResults,
  loadGroup
} from './tournament.actions'
import {SnackbarService} from '../services/snackbar/snackbar.service'
import {LeaderboardService} from '../services/leaderboard/leaderboard.service'
import {ServiceError} from '../../model/error'
import {DashboardService} from '../../dashboard/dashboard.service'
import {Router} from '@angular/router'

// noinspection JSUnusedGlobalSymbols
@Injectable({
  providedIn: 'root'
})
export class TournamentEffects {
  private actions$ = inject(Actions)
  private router = inject(Router)
  private groupService = inject(GroupService)
  private leaderboardService = inject(LeaderboardService)
  private snackbarService = inject(SnackbarService)
  private dashboardService = inject(DashboardService)

  getOverviewGroups = createEffect(() => this.actions$.pipe(
    ofType(getOverviewGroups),
    exhaustMap(() => {
      return this.groupService.getGroups().pipe(map(groups => {
        return overviewGroupsLoaded({groups})
      }))
    })
  ))

  selectGroup = createEffect(() => this.actions$.pipe(
    ofType(loadGroup),
    exhaustMap(action => {
      return this.groupService.getGroup(action.groupId).pipe(map(group => {
        return groupLoaded({group})
      }))
    })
  ))

  saveBets = createEffect(() => this.actions$.pipe(
    ofType(saveBets),
    exhaustMap(action => {
      return this.groupService.saveBets(action.groupId, action.bets).pipe(
        map(group => {
          return betsSaved({group})
        }),
        catchError((err: ServiceError) => {
          this.snackbarService.addMessage({
            type: 'error',
            message: err.error.displayMessage
          })
          return of(resetSaving())
        })
      )
    })
  ))

  betsSaved = createEffect(() => this.actions$.pipe(
    ofType(betsSaved),
    tap(() => {
      this.snackbarService.addMessage({
        type: 'success',
        message: 'Erfolgreich gespeichert'
      })
      this.router.navigate(['bets']).then()
    })
  ), {dispatch: false})

  saveResults = createEffect(() => this.actions$.pipe(
    ofType(saveResults),
    exhaustMap(action => {
      return this.groupService.saveResults(action.groupId, action.results).pipe(
        map(group => {
          return resultsSaved({group})
        }),
        catchError(() => {
          this.snackbarService.addMessage({
            type: 'error',
            message: 'Fehler beim Speichern'
          })
          return of(resetSaving())
        })
      )
    })
  ))

  resultsSaved = createEffect(() => this.actions$.pipe(
    ofType(resultsSaved),
    tap(() => {
      this.snackbarService.addMessage({
        type: 'success',
        message: 'Erfolgreich gespeichert'
      })
      this.router.navigate(['admin/results']).then()
    })
  ), {dispatch: false})

  getLeaderboard = createEffect(() => this.actions$.pipe(
    ofType(getLeaderboard),
    exhaustMap(() => {
      return this.leaderboardService.getLeaderboard().pipe(
        map(leaderboard => leaderboardLoaded({leaderboard}))
      )
    })
  ))

  loadDashboardData = createEffect(() => this.actions$.pipe(
    ofType(loadDashboardData),
    exhaustMap(() => {
      return this.dashboardService.loadDashboardData().pipe(
        map(data => dashboardDataLoaded({data}))
      )
    })
  ))
}
