import {ComponentFixture, TestBed} from '@angular/core/testing'

import {NotificationsComponent} from './notifications.component'
import {MockStore, provideMockStore} from '@ngrx/store/testing'
import {selectNotifications} from '../../../../../user-management/store/user.feature'

describe('NotificationsComponent', () => {
  let component: NotificationsComponent
  let fixture: ComponentFixture<NotificationsComponent>
  let store: MockStore

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [NotificationsComponent],
      providers: [provideMockStore()]
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
})
