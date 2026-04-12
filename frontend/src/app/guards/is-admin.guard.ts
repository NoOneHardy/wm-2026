import {CanActivateFn, Router} from '@angular/router'
import {inject} from '@angular/core'
import {Store} from '@ngrx/store'
import {selectIsAdmin, selectIsUserLoading} from '../user-management/store/user.feature'
import {combineLatest, filter, map, Observable} from 'rxjs'

export const isAdminGuard: CanActivateFn = () => {
  const store = inject(Store)
  const router = inject(Router)

  const isAdmin$: Observable<boolean> = store.select(selectIsAdmin)
  const isLoading$: Observable<boolean> = store.select(selectIsUserLoading)

  return combineLatest([isAdmin$, isLoading$]).pipe(
    filter(([, isLoading]) => !isLoading),
    map(([isAdmin]) => {
      return isAdmin ? true : router.createUrlTree(['/'])
    })
  )
}
