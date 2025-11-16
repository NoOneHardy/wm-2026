import {ComponentFixture, TestBed} from '@angular/core/testing'

import {AccountSettingsComponent} from './account-settings.component'
import {provideMockStore} from '@ngrx/store/testing'

describe('AccountSettingsComponent', () => {
  let component: AccountSettingsComponent
  let fixture: ComponentFixture<AccountSettingsComponent>

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [AccountSettingsComponent],
      providers: [provideMockStore()]
    }).compileComponents()

    fixture = TestBed.createComponent(AccountSettingsComponent)
    component = fixture.componentInstance
    fixture.detectChanges()
  })

  it('should create', () => {
    expect(component).toBeTruthy()
  })
})
