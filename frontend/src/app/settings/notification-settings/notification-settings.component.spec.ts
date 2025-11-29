import {ComponentFixture, TestBed} from '@angular/core/testing'

import {NotificationSettingsComponent} from './notification-settings.component'
import {MockStore, provideMockStore} from '@ngrx/store/testing'
import {selectNotificationPreferences} from '../../user-management/store/user.feature'
import {NotificationPreference} from '../../user-management/model/notification-preference'
import {NotificationType} from '../../user-management/model/notification-type'
import {NotificationChannel} from '../../user-management/model/notification-channel'
import {updateNotificationPreferences} from '../../user-management/store/user.actions'

describe('EmailSettingsComponent', () => {
  let component: NotificationSettingsComponent
  let fixture: ComponentFixture<NotificationSettingsComponent>
  let store: MockStore

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [NotificationSettingsComponent],
      providers: [provideMockStore()]
    }).compileComponents()

    store = TestBed.inject(MockStore)
    store.overrideSelector(selectNotificationPreferences, [])

    fixture = TestBed.createComponent(NotificationSettingsComponent)
    component = fixture.componentInstance
    fixture.detectChanges()
  })

  it('should create', () => {
    expect(component).toBeTruthy()
  })

  it('should load notification preferences from store', () => {
    expect(component.notifications()).toEqual([])

    const mockPreferences: NotificationPreference[] = [
      {
        type: NotificationType.NEW_RESULT,
        channel: NotificationChannel.EMAIL,
        selected: true
      }
    ]
    store.overrideSelector(selectNotificationPreferences, mockPreferences)
    store.refreshState()
    fixture.detectChanges()

    expect(component.notifications()).toEqual(mockPreferences)
  })

  it('should initialize form groups based on notification preferences', () => {
    const mockPreferences: NotificationPreference[] = [
      {
        type: NotificationType.NEW_RESULT,
        channel: NotificationChannel.EMAIL,
        selected: true
      },
      {
        type: NotificationType.NEW_GAME,
        channel: NotificationChannel.EMAIL,
        selected: false
      },
      {
        type: NotificationType.APPROVAL,
        channel: NotificationChannel.IN_APP,
        selected: true
      }
    ]
    store.overrideSelector(selectNotificationPreferences, mockPreferences)
    store.refreshState()
    fixture.detectChanges()

    const channelsArray = component.form.controls.channels
    expect(channelsArray.length).toBe(2)

    const emailGroup = channelsArray.controls.find(g => g.controls.data.value === NotificationChannel.EMAIL)
    const inAppGroup = channelsArray.controls.find(g => g.controls.data.value === NotificationChannel.IN_APP)

    expect(emailGroup).toBeDefined()
    expect(emailGroup?.controls.preferences.length).toBe(2)

    expect(inAppGroup).toBeDefined()
    expect(inAppGroup?.controls.preferences.length).toBe(1)

    if (!emailGroup || !inAppGroup) return

    const emailGroupPreferences = emailGroup.controls.preferences.controls
    expect(emailGroupPreferences[0].controls.data.value).toEqual({
      type: NotificationType.NEW_RESULT,
      channel: NotificationChannel.EMAIL,
      selected: true
    })
    expect(emailGroupPreferences[0].controls.enabled.value).toBeTrue()
    expect(emailGroupPreferences[1].controls.data.value).toEqual({
      type: NotificationType.NEW_GAME,
      channel: NotificationChannel.EMAIL,
      selected: false
    })
    expect(emailGroupPreferences[1].controls.enabled.value).toBeFalse()

    const inAppGroupPreferences = inAppGroup.controls.preferences.controls
    expect(inAppGroupPreferences[0].controls.data.value).toEqual({
      type: NotificationType.APPROVAL,
      channel: NotificationChannel.IN_APP,
      selected: true
    })
    expect(inAppGroupPreferences[0].controls.enabled.value).toBeTrue()
  })

  it('should dispatch updated preferences on save', () => {
    const spy = spyOn(store, 'dispatch')

    const mockPreferences: NotificationPreference[] = [
      {
        type: NotificationType.NEW_RESULT,
        channel: NotificationChannel.EMAIL,
        selected: true
      },
      {
        type: NotificationType.NEW_GAME,
        channel: NotificationChannel.EMAIL,
        selected: false
      }
    ]
    store.overrideSelector(selectNotificationPreferences, mockPreferences)
    store.refreshState()
    fixture.detectChanges()

    component.save()

    expect(spy).toHaveBeenCalledOnceWith(updateNotificationPreferences({preferences: mockPreferences}))
  })

  it('should update form controls when channel is disabled', () => {
    const mockPreferences: NotificationPreference[] = [
      {
        type: NotificationType.NEW_RESULT,
        channel: NotificationChannel.EMAIL,
        selected: true
      },
      {
        type: NotificationType.NEW_GAME,
        channel: NotificationChannel.EMAIL,
        selected: false
      }
    ]
    store.overrideSelector(selectNotificationPreferences, mockPreferences)
    store.refreshState()
    fixture.detectChanges()

    const channelsArray = component.form.controls.channels
    const emailGroup = channelsArray.controls.find(g => g.controls.data.value === NotificationChannel.EMAIL)
    if (!emailGroup) return

    emailGroup.controls.enabled.setValue(false)
    fixture.detectChanges()

    const emailPreferences = emailGroup.controls.preferences.controls
    expect(emailPreferences[0].controls.enabled.value).toBeFalse()
    expect(emailPreferences[1].controls.enabled.value).toBeFalse()
  })

  it('should update form controls when channel is enabled', () => {
    const mockPreferences: NotificationPreference[] = [
      {
        type: NotificationType.NEW_RESULT,
        channel: NotificationChannel.EMAIL,
        selected: false
      },
      {
        type: NotificationType.NEW_GAME,
        channel: NotificationChannel.EMAIL,
        selected: false
      }
    ]
    store.overrideSelector(selectNotificationPreferences, mockPreferences)
    store.refreshState()
    fixture.detectChanges()

    const channelsArray = component.form.controls.channels
    const emailGroup = channelsArray.controls.find(g => g.controls.data.value === NotificationChannel.EMAIL)
    if (!emailGroup) return

    emailGroup.controls.enabled.setValue(true)
    fixture.detectChanges()

    const emailPreferences = emailGroup.controls.preferences.controls
    expect(emailPreferences[0].controls.enabled.value).toBeTrue()
    expect(emailPreferences[1].controls.enabled.value).toBeTrue()
  })

  it('should reset form on notification preferences change', () => {
    const mockPreferences1: NotificationPreference[] = [
      {
        type: NotificationType.NEW_RESULT,
        channel: NotificationChannel.EMAIL,
        selected: true
      }
    ]
    store.overrideSelector(selectNotificationPreferences, mockPreferences1)
    store.refreshState()
    fixture.detectChanges()

    expect(component.form.controls.channels.length).toBe(1)

    const mockPreferences2: NotificationPreference[] = [
      {
        type: NotificationType.NEW_GAME,
        channel: NotificationChannel.IN_APP,
        selected: false
      },
      {
        type: NotificationType.APPROVAL,
        channel: NotificationChannel.EMAIL,
        selected: true
      }
    ]
    store.overrideSelector(selectNotificationPreferences, mockPreferences2)
    store.refreshState()
    fixture.detectChanges()

    expect(component.form.controls.channels.length).toBe(2)
  })

  it('should reset form on notification preferences change and remove old channels', () => {
    const mockPreferences1: NotificationPreference[] = [
      {
        type: NotificationType.NEW_RESULT,
        channel: NotificationChannel.EMAIL,
        selected: true
      }
    ]
    store.overrideSelector(selectNotificationPreferences, mockPreferences1)
    store.refreshState()
    fixture.detectChanges()

    expect(component.form.controls.channels.length).toBe(1)

    const mockPreferences2: NotificationPreference[] = [
      {
        type: NotificationType.NEW_GAME,
        channel: NotificationChannel.IN_APP,
        selected: false
      }
    ]
    store.overrideSelector(selectNotificationPreferences, mockPreferences2)
    store.refreshState()
    fixture.detectChanges()

    expect(component.form.controls.channels.length).toBe(1)
    expect(component.form.controls.channels.at(0)?.controls.data.value).toBe(NotificationChannel.IN_APP)
  })
})
