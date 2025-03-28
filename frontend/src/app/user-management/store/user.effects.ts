import {inject, Injectable} from '@angular/core'
import {Actions, createEffect, ofType} from '@ngrx/effects'
import {UserService} from '../user.service'
import {createUser, fetchUserInfo, userCreated, userInfoFetched, userLoggedIn, userLogin} from './user.actions'
import {exhaustMap, map} from 'rxjs'
import {Router} from '@angular/router'

@Injectable({
  providedIn: 'root'
})
export class UserEffects {
  private actions$ = inject(Actions)
  private userService = inject(UserService)
  private router = inject(Router)

  signup = createEffect(() => this.actions$.pipe(
    ofType(createUser),
    exhaustMap(action => {
      return this.userService.createUser(action.user).pipe(map(() => {
          return userCreated()
        })
      )
    })
  ))

  login = createEffect(() => this.actions$.pipe(
    ofType(userLogin),
    exhaustMap(action => {
      return this.userService.login(action).pipe(map((res) => {
        return userLoggedIn(res)
      }))
    })
  ))

  loggedIn = createEffect(() => this.actions$.pipe(
    ofType(userLoggedIn),
    map(() => {
      this.router.navigateByUrl('/').then()
      return fetchUserInfo()
    })
  ))

  fetchUserInfo = createEffect(() => this.actions$.pipe(
    ofType(fetchUserInfo),
    exhaustMap(() => {
      return this.userService.fetchUserInfo().pipe(map((user) => {
        return userInfoFetched({user})
      }))
    })
  ))
}
