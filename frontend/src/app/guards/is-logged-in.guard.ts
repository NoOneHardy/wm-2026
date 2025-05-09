import {inject} from '@angular/core'
import {Store} from '@ngrx/store'
import {selectUser} from '../user-management/store/user.feature'
import {CanActivateFn, Router} from '@angular/router'

export const isLoggedInGuard: CanActivateFn = () => {
  const store = inject(Store)
  const router = inject(Router)

  const user = store.selectSignal(selectUser)

  return user() ? true : router.createUrlTree(['/'])
}
