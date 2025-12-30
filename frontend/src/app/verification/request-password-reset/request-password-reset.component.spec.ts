import {ComponentFixture, TestBed} from '@angular/core/testing'

import {RequestPasswordResetComponent} from './request-password-reset.component'
import {MockStore, provideMockStore} from '@ngrx/store/testing'
import {selectIsUserLoading} from '../../user-management/store/user.feature'
import {requestPasswordResetLink} from '../../user-management/store/user.actions'

describe('RequestPasswordResetComponent', () => {
  let component: RequestPasswordResetComponent
  let fixture: ComponentFixture<RequestPasswordResetComponent>

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [RequestPasswordResetComponent],
      providers: [provideMockStore()]
    }).compileComponents()

    fixture = TestBed.createComponent(RequestPasswordResetComponent)
    component = fixture.componentInstance
    fixture.detectChanges()
  })

  it('should create', () => {
    expect(component).toBeTruthy()
  })

  it('should load isLoading from store', () => {
    expect(component.isLoading).toBeDefined()
    const store = TestBed.inject(MockStore)
    store.overrideSelector(selectIsUserLoading, false)
    store.refreshState()
    fixture.detectChanges()

    expect(component.isLoading()).toBeFalse()

    store.overrideSelector(selectIsUserLoading, true)
    store.refreshState()
    fixture.detectChanges()
    expect(component.isLoading()).toBeTrue()
  })

  it('should have initial state as FORM', () => {
    expect(component.state).toBe(0) // STATE.FORM is 0
  })

  it('should not submit if form is invalid', () => {
    const spy = spyOn(component['store'], 'dispatch')
    component.submit()
    expect(spy).not.toHaveBeenCalled()
  })

  it('should call dispatch requestPasswordResetLink action on valid form submission', () => {
    const spy = spyOn(component['store'], 'dispatch')
    component.formGroup.controls.email.setValue('silas@test.ch')
    component.submit()
    expect(spy).toHaveBeenCalledWith(requestPasswordResetLink({email: 'silas@test.ch'}))
  })

  it('should change state to CONFIRMATION on successful submission', () => {
    component.formGroup.controls.email.setValue('silas@test.ch')
    component.submit()
    expect(component.state).toBe(1) // STATE.CONFIRMATION is 1
  })
})
