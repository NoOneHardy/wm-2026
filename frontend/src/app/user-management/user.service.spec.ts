import {TestBed} from '@angular/core/testing'
import {provideMockStore} from '@ngrx/store/testing'
import {provideHttpClient} from '@angular/common/http'
import {HttpTestingController, provideHttpClientTesting} from '@angular/common/http/testing'
import {ServiceError} from '../model/error'
import {UserService} from './user.service'
import {AvailabilityCheck} from './model/availability-check'
import {flushApiErrorResponse, flushApiResponse} from '../shared/helper/karma.helper'

describe('UserService', () => {
  let service: UserService
  let httpMock: HttpTestingController

  beforeEach(() => {
    TestBed.configureTestingModule({
      providers: [provideHttpClient(), provideHttpClientTesting(), provideMockStore()]
    })
    service = TestBed.inject(UserService)
    httpMock = TestBed.inject(HttpTestingController)
  })

  afterEach(() => {
    httpMock.verify()
  })

  it('should be created', () => {
    expect(service).toBeTruthy()
  })

  it('should check username availability', () => {
    const username = 'test-user'
    const res: AvailabilityCheck = {
      isEmailAvailable: true,
      isUsernameAvailable: false
    }
    service.checkUsername(username).subscribe(response => {
      expect(response).toEqual(res)
    })

    const req = httpMock.expectOne(`/api/user/check?username=${username}`)
    expect(req.request.method).toBe('GET')
    flushApiResponse(req, {isEmailAvailable: true, isUsernameAvailable: false})
  })

  it('should throw error on check username availability', () => {
    const username = 'test-user'

    service.checkUsername(username).subscribe({
      error: (err: ServiceError) => {
        expect(err.error.message).toBe('Internal Server Error')
        expect(err.status).toBe(500)
      }
    })

    const req = httpMock.expectOne(`/api/user/check?username=${username}`)
    expect(req.request.method).toBe('GET')
    flushApiErrorResponse(req)
  })

  it('should check email availability', () => {
    const email = 'test@no1hardy.ch'
    const res: AvailabilityCheck = {
      isEmailAvailable: true,
      isUsernameAvailable: true
    }
    service.checkEmail(email).subscribe(response => {
      expect(response).toEqual(res)
    })

    const req = httpMock.expectOne(`/api/user/check?email=${email.replace('@', '%40')}`)
    expect(req.request.method).toBe('GET')
    flushApiResponse(req, {isEmailAvailable: true, isUsernameAvailable: true})
  })

  it('should throw error on check email availability', () => {
    const email = 'test@no1hardy.ch'

    service.checkEmail(email).subscribe({
      error: (err: ServiceError) => {
        expect(err.error.message).toBe('Internal Server Error')
        expect(err.status).toBe(500)
      }
    })

    const req = httpMock.expectOne(`/api/user/check?email=${email.replace('@', '%40')}`)
    expect(req.request.method).toBe('GET')
    flushApiErrorResponse(req)
  })

  it('should mark notification as read', () => {
    const notificationId = 'notification-1'
    service.markNotificationAsRead(notificationId).subscribe(response => {
      expect(response).toBeTrue()
    })

    const req = httpMock.expectOne(`/api/notification/${notificationId}`)
    expect(req.request.method).toBe('DELETE')
    flushApiResponse(req, true)
  })

  it('should throw error when marking notification as read', () => {
    const notificationId = 'notification-1'
    service.markNotificationAsRead(notificationId).subscribe({
      error: (err: ServiceError) => {
        expect(err.error.status).toBe(500)
        expect(err.error.message).toEqual('Internal Server Error')
      }
    })

    const req = httpMock.expectOne(`/api/notification/${notificationId}`)
    expect(req.request.method).toBe('DELETE')
    flushApiErrorResponse(req)
  })
})
