import {ComponentFixture, TestBed} from '@angular/core/testing'

import {NotificationsComponent} from './notifications.component'
import {MockStore, provideMockStore} from '@ngrx/store/testing'
import {selectNotifications} from '../../../../../user-management/store/user.feature'
import {NotificationType} from '../../../../../user-management/model/notification'
import {provideRouter} from '@angular/router'
import {markNotificationAsRead} from '../../../../../user-management/store/user.actions'

describe('NotificationsComponent', () => {
  let component: NotificationsComponent
  let fixture: ComponentFixture<NotificationsComponent>
  let store: MockStore

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [NotificationsComponent],
      providers: [provideMockStore(), provideRouter([])]
    }).compileComponents()

    fixture = TestBed.createComponent(NotificationsComponent)
    component = fixture.componentInstance
    store = TestBed.inject(MockStore)
    store.overrideSelector(selectNotifications, [])
    fixture.detectChanges()
  })

  it('should create', () => {
    expect(component).toBeTruthy()
  })

  it('should load notifications from store', () => {
    expect(component.notifications()).toEqual([])

    store.overrideSelector(selectNotifications, [{
      id: 'not-1',
      title: 'Test Notification',
      content: 'Empty',
      route: '/',
      type: NotificationType.NEW_RESULT
    }])
    store.refreshState()
    fixture.detectChanges()

    expect(component.notifications()).toEqual([{
      id: 'not-1',
      title: 'Test Notification',
      content: 'Empty',
      route: '/',
      type: NotificationType.NEW_RESULT
    }])
  })

  it('should map mobile input to boolean', () => {
    expect(component.mobile()).toBeFalse()
    fixture.componentRef.setInput('mobile', '')
    fixture.detectChanges()
    expect(component.mobile()).toBeTrue()
    fixture.componentRef.setInput('mobile', false)
    fixture.detectChanges()
    expect(component.mobile()).toBeFalse()
    fixture.componentRef.setInput('mobile', true)
    fixture.detectChanges()
    expect(component.mobile()).toBeTrue()
  })

  it('should call mark as read from store', () => {
    const spy = spyOn(store, 'dispatch')
    expect(spy).not.toHaveBeenCalled()

    component.markAsRead('asdfsaf')
    expect(spy).toHaveBeenCalledOnceWith(markNotificationAsRead({id: 'asdfsaf'}))
  })

  it('should map notifications length to boolean', () => {
    expect(component.hasNotifications()).toBeFalse()

    store.overrideSelector(selectNotifications, [{
      id: 'not-1',
      title: 'Test Notification',
      content: 'Empty',
      route: '/',
      type: NotificationType.NEW_RESULT
    }])
    store.refreshState()
    fixture.detectChanges()
    expect(component.hasNotifications()).toBeTrue()
  })

  it('should return correct icon for notification type', () => {
    expect(component.getIcon(NotificationType.NEW_GAME)).toBe('ballot')
    expect(component.getIcon(NotificationType.NEW_RESULT)).toBe('scoreboard')
    expect(component.getIcon(NotificationType.APPROVAL)).toBe('check_circle')
    expect(component.getIcon(NotificationType.REJECTION)).toBe('do_not_disturb_on')
    expect(component.getIcon(NotificationType.RANKING_UPDATE)).toBe('bookmark_star')
  })
})
