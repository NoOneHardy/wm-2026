import {TestBed} from '@angular/core/testing'
import {ActivatedRouteSnapshot, CanActivateFn, provideRouter, Router, RouterStateSnapshot} from '@angular/router'

import {isAdminGuard} from './is-admin.guard'
import {MockStore, provideMockStore} from '@ngrx/store/testing'
import {selectIsAdmin} from '../user-management/store/user.feature'

describe('isAdminGuard', () => {
  const executeGuard: CanActivateFn = (...guardParameters) =>
    TestBed.runInInjectionContext(() => isAdminGuard(...guardParameters))
  const route: ActivatedRouteSnapshot = {} as ActivatedRouteSnapshot
  const state: RouterStateSnapshot = {} as RouterStateSnapshot
  let store: MockStore

  beforeEach(() => {
    TestBed.configureTestingModule({
      providers: [provideRouter([]), provideMockStore()]
    })

    store = TestBed.inject(MockStore)
  })

  it('should be created', () => {
    expect(executeGuard).toBeTruthy()
  })

  it('should return true if user is admin', () => {
    store.overrideSelector(selectIsAdmin, true)
    const result = executeGuard(route , state)
    expect(result).toBe(true)
  })

  it('should return a url tree for "/" if user is not admin', () => {
    store.overrideSelector(selectIsAdmin, false)
    const result = executeGuard(route , state)
    expect(result).toEqual(TestBed.inject(Router).createUrlTree(['/']))
  })
})
