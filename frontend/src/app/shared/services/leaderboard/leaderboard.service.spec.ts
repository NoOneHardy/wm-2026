import {TestBed} from '@angular/core/testing'

import {LeaderboardService} from './leaderboard.service'
import {provideHttpClient} from '@angular/common/http'
import {provideMockStore} from '@ngrx/store/testing'

describe('LeaderboardService', () => {
  let service: LeaderboardService

  beforeEach(() => {
    TestBed.configureTestingModule({
      providers: [provideHttpClient(), provideMockStore()]
    })
    service = TestBed.inject(LeaderboardService)
  })

  it('should be created', () => {
    expect(service).toBeTruthy()
  })
})
