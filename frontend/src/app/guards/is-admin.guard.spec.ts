import {TestBed} from '@angular/core/testing'
import {ActivatedRouteSnapshot, GuardResult, provideRouter, Router, RouterStateSnapshot} from '@angular/router'

import {isAdminGuard} from './is-admin.guard'
import {MockStore, provideMockStore} from '@ngrx/store/testing'
import {selectIsAdmin, selectIsUserLoading} from '../user-management/store/user.feature'
import {Observable} from 'rxjs'

describe('isAdminGuard', () => {
  const route: ActivatedRouteSnapshot = {} as ActivatedRouteSnapshot
  const state: RouterStateSnapshot = {} as RouterStateSnapshot
  let store: MockStore
  let result: GuardResult | null

  const subscribeToGuard = () => {
    const guard$: Observable<GuardResult> = TestBed.runInInjectionContext(() => isAdminGuard(route, state)) as Observable<GuardResult>
    guard$.subscribe((guardResult) => {
      result = guardResult
    })
  }

  beforeEach(() => {
    TestBed.configureTestingModule({
      providers: [provideRouter([]), provideMockStore()]
    })

    result = null
    store = TestBed.inject(MockStore)
  })

  it('should return true if user is admin', () => {
    store.overrideSelector(selectIsAdmin, true)
    store.refreshState()
    subscribeToGuard()

    expect(result).toBe(true)
  })

  it('should return a url tree for "/" if user is not admin', () => {
    store.overrideSelector(selectIsAdmin, false)
    store.refreshState()
    subscribeToGuard()
    expect(result).toEqual(TestBed.inject(Router).createUrlTree(['/']))
  })

  it('should wait until loading is done and redirect', () => {
    store.overrideSelector(selectIsAdmin, false)
    store.overrideSelector(selectIsUserLoading, true)
    store.refreshState()
    subscribeToGuard()
    expect(result).toBeNull()

    store.overrideSelector(selectIsUserLoading, false)
    store.refreshState()
    expect(result).toEqual(TestBed.inject(Router).createUrlTree(['/']))
  })

  it('should wait until loading is done and allow', () => {
    store.overrideSelector(selectIsAdmin, false)
    store.overrideSelector(selectIsUserLoading, true)
    store.refreshState()
    subscribeToGuard()
    expect(result).toBeNull()

    store.overrideSelector(selectIsUserLoading, false)
    store.overrideSelector(selectIsAdmin, true)
    store.refreshState()
    expect(result).toBeTrue()
  })
})
