import {TestBed} from '@angular/core/testing'

import {AuthHttpService} from './auth-http.service'
import {provideMockStore} from '@ngrx/store/testing'
import {provideHttpClient} from '@angular/common/http'

describe('AuthHttpService', () => {
  let service: AuthHttpService

  beforeEach(() => {
    TestBed.configureTestingModule({
      providers: [provideMockStore(), provideHttpClient()]
    })
    service = TestBed.inject(AuthHttpService)
  })

  it('should be created', () => {
    expect(service).toBeTruthy()
  })
})
