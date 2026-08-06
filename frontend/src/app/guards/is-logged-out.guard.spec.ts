// noinspection DuplicatedCode

import {TestBed} from '@angular/core/testing'
import {ActivatedRouteSnapshot, GuardResult, provideRouter, Router, RouterStateSnapshot} from '@angular/router'

import {MockStore, provideMockStore} from '@ngrx/store/testing'
import {selectIsUserLoading, selectUser} from '../user-management/store/user.feature'
import {Observable} from 'rxjs'
import {User} from '../model/user/user'
import {mockUser1} from '../model/mock/user.mock'
import {isLoggedOutGuard} from './is-logged-out.guard'

describe('isLoggedInGuard', () => {
  const route: ActivatedRouteSnapshot = {} as ActivatedRouteSnapshot
  const state: RouterStateSnapshot = {} as RouterStateSnapshot
  let store: MockStore
  let result: GuardResult | null
  let mockUser: User

  const subscribeToGuard = () => {
    const guard$: Observable<GuardResult> = TestBed.runInInjectionContext(() => isLoggedOutGuard(route, state)) as Observable<GuardResult>
    guard$.subscribe((guardResult) => {
      result = guardResult
    })
  }

  beforeEach(() => {
    TestBed.configureTestingModule({
      providers: [provideRouter([]), provideMockStore()]
    })

    mockUser = {...mockUser1}

    result = null
    store = TestBed.inject(MockStore)
    store.overrideSelector(selectUser, {...mockUser})
    store.overrideSelector(selectIsUserLoading, false)
  })

  it('should return true if user is not logged in', () => {
    store.overrideSelector(selectUser, null)
    store.refreshState()
    subscribeToGuard()
    expect(result).toBe(true)
  })

  it('should return a url tree for "/" if user is logged in', () => {
    subscribeToGuard()
    expect(result).toEqual(TestBed.inject(Router).createUrlTree(['/']))
  })

  it('should wait until loading is done and redirect', () => {
    store.overrideSelector(selectUser, null)
    store.overrideSelector(selectIsUserLoading, true)
    store.refreshState()

    subscribeToGuard()
    expect(result).toBeNull()

    store.overrideSelector(selectIsUserLoading, false)
    store.overrideSelector(selectUser, {...mockUser})
    store.refreshState()
    expect(result).toEqual(TestBed.inject(Router).createUrlTree(['/']))
  })

  it('should wait until loading is done and allow', () => {
    store.overrideSelector(selectUser, null)
    store.overrideSelector(selectIsUserLoading, true)
    store.refreshState()

    subscribeToGuard()
    expect(result).toBeNull()

    store.overrideSelector(selectIsUserLoading, false)
    store.refreshState()
    expect(result).toBe(true)
  })
})
