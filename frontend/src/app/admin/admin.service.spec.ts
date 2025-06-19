import {TestBed} from '@angular/core/testing'

import {AdminService} from './admin.service'
import {HttpTestingController, provideHttpClientTesting} from '@angular/common/http/testing'
import {User} from '../model/user/user'
import {provideHttpClient} from '@angular/common/http'
import {mockUser1} from '../model/mock/user.mock'

describe('AdminService', () => {
  let service: AdminService
  let httpMock: HttpTestingController

  beforeEach(() => {
    TestBed.configureTestingModule({
      providers: [provideHttpClient(), provideHttpClientTesting()]
    })
    service = TestBed.inject(AdminService)
    httpMock = TestBed.inject(HttpTestingController)
  })

  afterEach(() => {
    httpMock.verify()
  })

  it('should be created', () => {
    expect(service).toBeTruthy()
  })

  it('should return a list of users', () => {
    const mockUsers: User[] = [{...mockUser1}]

    service.getAllUsers().subscribe(users => {
      expect(users).toEqual(mockUsers)
    })

    const req = httpMock.expectOne('/api/user/all')
    expect(req.request.method).toBe('GET')
    req.flush(mockUsers)
  })

  it('should return an empty list if no users are found', () => {
    service.getAllUsers().subscribe(users => {
      expect(users).toEqual([])
    })

    const req = httpMock.expectOne('/api/user/all')
    expect(req.request.method).toBe('GET')
    req.flush([])
  })
})
