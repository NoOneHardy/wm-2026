import {TestBed} from '@angular/core/testing'
import {ActivatedRouteSnapshot, CanActivateFn, provideRouter, Router, RouterStateSnapshot} from '@angular/router'

import {isLoggedInGuard} from './is-logged-in.guard'
import {MockStore, provideMockStore} from '@ngrx/store/testing'
import {selectUser} from '../user-management/store/user.feature'

describe('isLoggedInGuard', () => {
  const executeGuard: CanActivateFn = (...guardParameters) =>
    TestBed.runInInjectionContext(() => isLoggedInGuard(...guardParameters))
  const route: ActivatedRouteSnapshot = {} as ActivatedRouteSnapshot
  const state: RouterStateSnapshot = {} as RouterStateSnapshot
  let store: MockStore

  beforeEach(() => {
    TestBed.configureTestingModule({
      providers: [provideRouter([]), provideMockStore()]
    })

    store = TestBed.inject(MockStore)
    store.overrideSelector(selectUser, {
      id: 'user-1',
      username: 'User',
      email: 'user@no1hardy.ch',
      firstname: 'Silas',
      lastname: 'No1hardy',
      isActive: true,
      points: 0,
      createdAt: new Date(),
      updatedAt: new Date()
    })
  })

  it('should be created', () => {
    expect(executeGuard).toBeTruthy()
  })

  it('should return true if user is logged in', () => {
    const result = executeGuard(route , state)
    expect(result).toBe(true)
  })

  it('should return a url tree for "/" if user is not logged in', () => {
    store.overrideSelector(selectUser, null)
    const result = executeGuard(route , state)
    expect(result).toEqual(TestBed.inject(Router).createUrlTree(['/']))
  })
})
