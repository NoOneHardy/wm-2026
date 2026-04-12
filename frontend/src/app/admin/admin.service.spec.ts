import {TestBed} from '@angular/core/testing'

import {AdminService} from './admin.service'
import {HttpTestingController, provideHttpClientTesting} from '@angular/common/http/testing'
import {User} from '../model/user/user'
import {provideHttpClient} from '@angular/common/http'
import {mockUser1} from '../model/mock/user.mock'
import {ServiceError} from '../model/error'
import {provideMockStore} from '@ngrx/store/testing'
import {flushApiErrorResponse, flushApiResponse} from '../shared/helper/karma.helper'
import {LightTeam} from '../model/team/light-team'
import {GroupOption} from './model/group-option'

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

  it('should return all teams for admin selectors', () => {
    const teams: LightTeam[] = [{
      id: 'team-1',
      name: 'Germany',
      shortName: 'GER',
      flag: '/cdn/flags/germany.png'
    }]

    service.getTeams().subscribe(response => {
      expect(response).toEqual(teams)
    })

    const req = httpMock.expectOne('/api/team')
    expect(req.request.method).toBe('GET')
    flushApiResponse(req, teams)
  })

  it('should return all groups for admin selectors', () => {
    const groups: GroupOption[] = [{
      id: 'group-1',
      name: 'Gruppe A',
      order: 1,
      isKnockout: false,
      thumbnail: null
    }]

    service.getGroupOptions().subscribe(response => {
      expect(response).toEqual(groups)
    })

    const req = httpMock.expectOne('/api/group/all')
    expect(req.request.method).toBe('GET')
    flushApiResponse(req, groups)
  })

  it('should upload a team flag', () => {
    const file = new File(['flag'], 'flag.png', {type: 'image/png'})
    service.uploadFlag(file).subscribe(url => {
      expect(url).toBe('/cdn/flags/flag.png')
    })

    const req = httpMock.expectOne('/api/team/flag')
    expect(req.request.method).toBe('POST')
    expect(req.request.body instanceof FormData).toBeTrue()
    flushApiResponse(req, '/cdn/flags/flag.png')
  })
})
