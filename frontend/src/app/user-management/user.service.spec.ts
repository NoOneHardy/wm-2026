import {TestBed} from '@angular/core/testing'

import {UserService} from './user.service'
import {provideHttpClient} from '@angular/common/http'
import {provideMockStore} from '@ngrx/store/testing'

describe('UserService', () => {
  let service: UserService

  beforeEach(() => {
    TestBed.configureTestingModule({
      providers: [provideHttpClient(), provideMockStore()]
    })
    service = TestBed.inject(UserService)
  })

  it('should be created', () => {
    expect(service).toBeTruthy()
  })
})
