import {TestBed} from '@angular/core/testing'

import {SnackbarService} from './snackbar.service'
import {provideMockStore} from '@ngrx/store/testing'

describe('SnackbarService', () => {
  let service: SnackbarService

  beforeEach(() => {
    TestBed.configureTestingModule({
      providers: [provideMockStore()]
    })
    service = TestBed.inject(SnackbarService)
  })

  it('should be created', () => {
    expect(service).toBeTruthy()
  })
})
