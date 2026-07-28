import {ComponentFixture, TestBed} from '@angular/core/testing'

import AccountSettingsComponent from './account-settings.component'
import {provideMockStore} from '@ngrx/store/testing'
import {provideHttpClient, withXhr} from '@angular/common/http'

describe('AccountSettingsComponent', () => {
  let component: AccountSettingsComponent
  let fixture: ComponentFixture<AccountSettingsComponent>

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [AccountSettingsComponent],
      providers: [provideMockStore(), provideHttpClient(withXhr())]
    }).compileComponents()

    fixture = TestBed.createComponent(AccountSettingsComponent)
    component = fixture.componentInstance
    fixture.detectChanges()
  })

  it('should create', () => {
    expect(component).toBeTruthy()
  })
})
