import {TestBed} from '@angular/core/testing'

import {GroupService} from './group.service'
import {provideHttpClient} from '@angular/common/http'
import {provideMockStore} from '@ngrx/store/testing'

describe('GroupService', () => {
  let service: GroupService

  beforeEach(() => {
    TestBed.configureTestingModule({
      providers: [provideHttpClient(), provideMockStore()]
    })
    service = TestBed.inject(GroupService)
  })

  it('should be created', () => {
    expect(service).toBeTruthy()
  })
})
