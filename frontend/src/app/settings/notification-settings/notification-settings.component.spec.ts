import {ComponentFixture, TestBed} from '@angular/core/testing'

import {NotificationSettingsComponent} from './notification-settings.component'
import {provideMockStore} from '@ngrx/store/testing'

describe('EmailSettingsComponent', () => {
  let component: NotificationSettingsComponent
  let fixture: ComponentFixture<NotificationSettingsComponent>

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [NotificationSettingsComponent],
      providers: [provideMockStore()]
    }).compileComponents()

    fixture = TestBed.createComponent(NotificationSettingsComponent)
    component = fixture.componentInstance
    fixture.detectChanges()
  })

  it('should create', () => {
    expect(component).toBeTruthy()
  })
})
