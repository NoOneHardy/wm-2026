import {inject} from '@angular/core'
import {Store} from '@ngrx/store'
import {selectIsUserLoading, selectUser} from '../user-management/store/user.feature'
import {CanActivateFn, Router} from '@angular/router'
import {combineLatest, filter, map, Observable} from 'rxjs'
import {User} from '../model/user/user'

export const isLoggedInGuard: CanActivateFn = () => {
  const store = inject(Store)
  const router = inject(Router)

  const user$: Observable<User | null> = store.select(selectUser)
  const isLoading: Observable<boolean> = store.select(selectIsUserLoading)

  return combineLatest([user$, isLoading]).pipe(
    filter(([, isLoading]) => !isLoading),
    map(([user]) => {
      return user ? true : router.createUrlTree(['/login'])
    })
  )
}
