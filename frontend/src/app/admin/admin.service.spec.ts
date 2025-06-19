import { TestBed } from '@angular/core/testing'

import { AdminService } from './admin.service'
import {provideHttpClient} from '@angular/common/http'

describe('AdminService', () => {
  let service: AdminService

  beforeEach(() => {
    TestBed.configureTestingModule({
      providers: [provideHttpClient()]
    })
    service = TestBed.inject(AdminService)
  })

  it('should be created', () => {
    expect(service).toBeTruthy()
  })
})
