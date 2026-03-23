import {ComponentFixture, TestBed} from '@angular/core/testing'

import {AppearanceSettingsComponent} from './appearance-settings.component'
import {AppearanceService} from '../../shared/services/appearance/appearance.service'

describe('AppearanceSettingsComponent', () => {
  let component: AppearanceSettingsComponent
  let fixture: ComponentFixture<AppearanceSettingsComponent>
  let appearanceService: AppearanceService

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [AppearanceSettingsComponent]
    }).compileComponents()

    appearanceService = TestBed.inject(AppearanceService)
    appearanceService.setTheme('light')

    fixture = TestBed.createComponent(AppearanceSettingsComponent)
    component = fixture.componentInstance
    fixture.detectChanges()
  })

  it('should create', () => {
    expect(component).toBeTruthy()
  })

  it('should switch to the floodlight mode', () => {
    const buttons = fixture.nativeElement.querySelectorAll('.mode-card') as NodeListOf<HTMLButtonElement>

    buttons[1].click()
    fixture.detectChanges()

    expect(appearanceService.theme()).toBe('dark')
  })
})
