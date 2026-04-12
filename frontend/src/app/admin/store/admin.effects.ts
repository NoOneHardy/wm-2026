import {inject, Injectable} from '@angular/core'
import {Actions, createEffect, ofType} from '@ngrx/effects'
import {AdminService} from '../admin.service'
import {applicationReviewed, confirmUser, denyUser, loadUsers, usersLoaded} from './admin.actions'
import {exhaustMap, map} from 'rxjs'

// noinspection JSUnusedGlobalSymbols
@Injectable({
  providedIn: 'root'
})
export class AdminEffects {
  private actions$ = inject(Actions)
  private adminService = inject(AdminService)

  loadUsers = createEffect(() => this.actions$.pipe(
    ofType(loadUsers),
    exhaustMap(() => {
      return this.adminService.getUsers().pipe(map(users => {
        return usersLoaded({users})
      }))
    })
  ))

  confirmUser = createEffect(() => this.actions$.pipe(
    ofType(confirmUser),
    exhaustMap((action) => {
      return this.adminService.confirmUser(action.id).pipe(map(user => {
        return applicationReviewed({user})
      }))
    })
  ))

  denyUser = createEffect(() => this.actions$.pipe(
    ofType(denyUser),
    exhaustMap((action) => {
      return this.adminService.denyUser(action.id).pipe(map(user => {
        return applicationReviewed({user})
      }))
    })
  ))
}
