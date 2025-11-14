import { ComponentFixture, TestBed } from '@angular/core/testing'

import { NotificiationSettingsComponent } from './notificiation-settings.component'

describe('EmailSettingsComponent', () => {
  let component: NotificiationSettingsComponent
  let fixture: ComponentFixture<NotificiationSettingsComponent>

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [NotificiationSettingsComponent]
    })
    .compileComponents()

    fixture = TestBed.createComponent(NotificiationSettingsComponent)
    component = fixture.componentInstance
    fixture.detectChanges()
  })

  it('should create', () => {
    expect(component).toBeTruthy()
  })
})
