import {CanActivateFn, Router} from '@angular/router'
import {inject} from '@angular/core'
import {Store} from '@ngrx/store'
import {selectIsAdmin} from '../user-management/store/user.feature'

export const isAdminGuard: CanActivateFn = () => {
  const store = inject(Store)
  const router = inject(Router)

  const isAdmin = store.selectSignal(selectIsAdmin)

  return isAdmin() ? true : router.createUrlTree(['/'])
}
