import {inject, Injectable} from '@angular/core'
import {Actions, createEffect, ofType} from '@ngrx/effects'
import {UserService} from '../user.service'
import {
  avatarUploaded,
  createUser,
  fetchUserInfo,
  loggedOut,
  logout,
  markedNotificationAsRead,
  markNotificationAsRead,
  rejectLogin,
  updateUser,
  uploadAvatar,
  userCreated,
  userInfoFetched,
  userLoggedIn,
  userLogin,
  userUpdated
} from './user.actions'
import {catchError, exhaustMap, map, of, tap} from 'rxjs'
import {Router} from '@angular/router'
import {SnackbarService} from '../../shared/services/snackbar/snackbar.service'
import {noAction} from '../../shared/store/global.actions'

// noinspection JSUnusedGlobalSymbols
@Injectable({
  providedIn: 'root'
})
export class UserEffects {
  private actions$ = inject(Actions)
  private userService = inject(UserService)
  private snackbarService = inject(SnackbarService)
  private router = inject(Router)

  signup = createEffect(() => this.actions$.pipe(
    ofType(createUser),
    exhaustMap(action => {
      return this.userService.createUser(action.user).pipe(map(() => {
          return userCreated({
            username: action.user.username,
            password: action.user.password
          })
        })
      )
    })
  ))

  userCreated = createEffect(() => this.actions$.pipe(
    ofType(userCreated),
    tap(() => this.snackbarService.addMessage({
      message: 'Benutzer erfolgreich erstellt',
    })),
    map((data) => userLogin(data))
  ))

  login = createEffect(() => this.actions$.pipe(
    ofType(userLogin),
    exhaustMap(action => {
      return this.userService.login(action).pipe(
        map((res) => {
          return userLoggedIn(res)
        }),
        catchError(() => {
          return of(rejectLogin())
        })
      )
    })
  ))

  loggedIn = createEffect(() => this.actions$.pipe(
    ofType(userLoggedIn),
    tap(() => {
      this.router.navigateByUrl('/').then()
    }),
    map(() => noAction())
  ))

  logout = createEffect(() => this.actions$.pipe(
    ofType(logout),
    exhaustMap(() => {
      return this.userService.logout().pipe(map(() => {
        return loggedOut({showMessage: true})
      }))
    })
  ))

  loggedOut = createEffect(() => this.actions$.pipe(
    ofType(loggedOut),
    tap(() => {
      this.snackbarService.addMessage({
        message: 'Erfolgreich abgemeldet',
      })
      this.router.navigateByUrl('/').then()
    }),
    map(() => noAction())
  ))

  fetchUserInfo = createEffect(() => this.actions$.pipe(
    ofType(fetchUserInfo),
    exhaustMap(() => {
      return this.userService.fetchUserInfo().pipe(map((user) => {
          return userInfoFetched({user})
        }),
        catchError(() => {
          return of(loggedOut({showMessage: false}))
        })
      )
    })
  ))

  markNotificationAsRead = createEffect(() => this.actions$.pipe(
    ofType(markNotificationAsRead),
    exhaustMap((action) => {
      return this.userService.markNotificationAsRead(action.id).pipe(
        map(() => markedNotificationAsRead())
      )
    })
  ))

  uploadAvatar = createEffect(() => this.actions$.pipe(
    ofType(uploadAvatar),
    exhaustMap((action) => {
      return this.userService.uploadAvatar(action.file).pipe(
        map((res) => avatarUploaded({avatarUrl: res.avatarUrl})),
        catchError(() => {
          this.snackbarService.addMessage({
            message: 'Avatar konnte nicht hochgeladen werden',
            type: 'error'
          })
          return of(noAction())
        })
      )
    })
  ))

  avatarUploaded = createEffect(() => this.actions$.pipe(
    ofType(avatarUploaded),
    tap(() => {
      this.snackbarService.addMessage({
        message: 'Avatar erfolgreich hochgeladen',
      })
    })
  ), {dispatch: false})

  updateUser = createEffect(() => this.actions$.pipe(
    ofType(updateUser),
    exhaustMap((action) => this.userService.updateUser(action.user).pipe(
      map((user) => userUpdated({user})),
    ))
  ))

  userUpdated = createEffect(() => this.actions$.pipe(
    ofType(userUpdated),
    tap(() => {
      this.snackbarService.addMessage({
        message: 'Benutzerinformationen erfolgreich aktualisiert'
      })
      this.router.navigateByUrl('/').then()
    }),
    map(action => userInfoFetched({user: action.user}))
  ))
}
