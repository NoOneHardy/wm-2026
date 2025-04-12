import {TestBed} from '@angular/core/testing'

import {SnackbarService} from './snackbar.service'
import {provideMockStore} from '@ngrx/store/testing'
import {SnackbarMessage} from '../../components/snackbar/model/snackbar-message'

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

  it('should provide messages', () => {
    const messages = service.messages
    expect(messages).toBeTruthy()
    expect(messages()).toEqual([])
  })

  it('should add a message', () => {
    const message: SnackbarMessage = {message: 'Test message', type: 'success'}
    service.addMessage(message)
    expect(service.messages()).toEqual([message])
  })

  it('should remove a message after 5000ms', () => {
    jasmine.clock().install()
    const message: SnackbarMessage = {message: 'Test message', type: 'success'}
    service.addMessage(message)
    expect(service.messages()).toEqual([message])

    jasmine.clock().tick(5000)
    expect(service.messages()).toEqual([])
    jasmine.clock().uninstall()
  })

  it('should remove a message after custom duration', () => {
    jasmine.clock().install()
    const message: SnackbarMessage = {message: 'Test message', duration: 1000}
    service.addMessage(message)
    expect(service.messages()).toEqual([message])

    jasmine.clock().tick(1000)
    expect(service.messages()).toEqual([])
    jasmine.clock().uninstall()
  })

  it('should handle multiple messages with different durations', () => {
    jasmine.clock().install()
    const message: SnackbarMessage = {message: 'Test message', duration: 1000}
    service.addMessage(message)
    expect(service.messages()).toEqual([message])

    const message2: SnackbarMessage = {message: 'Test message 2', duration: 7000}
    service.addMessage(message2)
    expect(service.messages()).toEqual([message, message2])

    jasmine.clock().tick(1000)
    expect(service.messages()).toEqual([message2])

    jasmine.clock().tick(6000)
    expect(service.messages()).toEqual([])
    jasmine.clock().uninstall()
  })
})
