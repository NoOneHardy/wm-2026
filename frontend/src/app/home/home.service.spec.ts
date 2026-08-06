import {TestBed} from '@angular/core/testing'
import {provideHttpClient, withXhr} from '@angular/common/http'
import {provideMockStore} from '@ngrx/store/testing'
import {HomeService} from './home.service'

describe('HomeService', () => {
  let service: HomeService

  beforeEach(() => {
    TestBed.configureTestingModule({
      providers: [provideHttpClient(withXhr()), provideMockStore()]
    })
    service = TestBed.inject(HomeService)
  })

  it('should be created', () => {
    expect(service).toBeTruthy()
  })
})
