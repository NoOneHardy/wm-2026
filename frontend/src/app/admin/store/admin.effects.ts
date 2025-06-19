import {inject, Injectable} from '@angular/core'
import {Actions, createEffect, ofType} from '@ngrx/effects'
import {AdminService} from '../admin.service'
import {loadUsers, usersLoaded} from './admin.actions'
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
      return this.adminService.getAllUsers().pipe(map(users => {
        return usersLoaded({users})
      }))
    })
  ))
}
