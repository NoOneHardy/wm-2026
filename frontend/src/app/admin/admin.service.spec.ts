import {TestBed} from '@angular/core/testing'

import {AdminService} from './admin.service'
import {HttpTestingController, provideHttpClientTesting} from '@angular/common/http/testing'
import {User} from '../model/user/user'
import {provideHttpClient} from '@angular/common/http'
import {mockUser1} from '../model/mock/user.mock'
import {ServiceError} from '../model/error'
import {provideMockStore} from '@ngrx/store/testing'
import {flushApiErrorResponse, flushApiResponse} from '../shared/helper/karma.helper'

describe('AdminService', () => {
  let service: AdminService
  let httpMock: HttpTestingController
  let mockUser: User

  beforeEach(() => {
    TestBed.configureTestingModule({
      providers: [provideHttpClient(), provideHttpClientTesting(), provideMockStore()]
    })
    service = TestBed.inject(AdminService)
    httpMock = TestBed.inject(HttpTestingController)

    mockUser = {...mockUser1}
  })

  afterEach(() => {
    httpMock.verify()
  })

  it('should be created', () => {
    expect(service).toBeTruthy()
  })

  it('should return a list of users', () => {
    const mockUsers: User[] = [mockUser]

    service.getUsers().subscribe(users => {
      expect(users).toEqual(mockUsers)
    })

    const req = httpMock.expectOne('/api/user')
    expect(req.request.method).toBe('GET')
    flushApiResponse(req, mockUsers)
  })

  it('should return an empty list if no users are found', () => {
    service.getUsers().subscribe(users => {
      expect(users).toEqual([])
    })

    const req = httpMock.expectOne('/api/user')
    expect(req.request.method).toBe('GET')
    flushApiResponse(req, [])
  })

  it('should confirm a user', () => {
    const userId = 'user-1'
    service.confirmUser(userId).subscribe(response => {
      expect(response).toBeTruthy()
      expect(response.id).toEqual(userId)
    })

    const req = httpMock.expectOne(`/api/admin/user/${userId}/confirm`)
    expect(req.request.method).toBe('GET')
    flushApiResponse(req, mockUser)
  })

  it('should return error message if user is not authenticated', () => {
    const userId = 'user-1'
    service.confirmUser(userId).subscribe({
      error: (err: ServiceError) => {
        expect(err.message).toEqual('Not authorized to access this method')
        expect(err.status).toEqual(403)
      }
    })

    const req = httpMock.expectOne(`/api/admin/user/${userId}/confirm`)
    expect(req.request.method).toBe('GET')
    flushApiErrorResponse(req, {
      status: 403,
      message: 'Not authorized to access this method'
    })
  })

  it('should deny a user', () => {
    const userId = 'user-1'
    service.denyUser(userId).subscribe(response => {
      expect(response).toBeTruthy()
      expect(response.id).toEqual(userId)
    })

    const req = httpMock.expectOne(`/api/admin/user/${userId}/deny`)
    expect(req.request.method).toBe('GET')
    flushApiResponse(req, mockUser)
  })

  it('should return error message if user is not authenticated', () => {
    const userId = 'user-1'
    service.denyUser(userId).subscribe({
      error: (err: ServiceError): void => {
        expect(err.message).toEqual('Not authorized to access this method')
        expect(err.status).toEqual(403)
      }
    })

    const req = httpMock.expectOne(`/api/admin/user/${userId}/deny`)
    expect(req.request.method).toBe('GET')
    flushApiErrorResponse(req, {
      status: 403,
      message: 'Not authorized to access this method'
    })
  })
})
