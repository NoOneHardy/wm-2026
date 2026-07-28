import {ComponentFixture, TestBed} from '@angular/core/testing'
import {vi} from 'vitest'

import {LoginComponent} from './login.component'
import {provideMockStore} from '@ngrx/store/testing'
import {provideRouter} from '@angular/router'
import {provideAnimations} from '@angular/platform-browser/animations'

describe('LoginComponent', () => {
  let component: LoginComponent
  let fixture: ComponentFixture<LoginComponent>

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [LoginComponent],
      providers: [provideMockStore(), provideRouter([]), provideAnimations()]
    }).compileComponents()

    fixture = TestBed.createComponent(LoginComponent)
    component = fixture.componentInstance
    fixture.detectChanges()
  })

  it('should create', () => {
    expect(component).toBeTruthy()
  })

  it('should have a form group', () => {
    expect(component.formGroup).toBeTruthy()
    expect(component.formGroup.get('username')).toBeTruthy()
    expect(component.formGroup.get('password')).toBeTruthy()
  })

  it('should validate username', () => {
    const usernameControl = component.formGroup.get('username')
    expect(usernameControl).toBeTruthy()
    expect(usernameControl?.valid).toBeFalsy()
    usernameControl?.setValue('user')
    expect(usernameControl?.valid).toBeTruthy()
  })

  it('should validate password', () => {
    const passwordControl = component.formGroup.get('password')
    expect(passwordControl).toBeTruthy()
    expect(passwordControl?.valid).toBeFalsy()
    passwordControl?.setValue('password')
    expect(passwordControl?.valid).toBeTruthy()
  })

  it('should mark form as touched on login', () => {
    vi.spyOn(component.formGroup, 'markAllAsTouched')
    component.login()
    expect(component.formGroup.markAllAsTouched).toHaveBeenCalled()
  })

  it('should call login on click', () => {
    vi.spyOn(component, 'login')
    fixture.nativeElement.querySelector('bet-button button').click()
    fixture.detectChanges()
    expect(component.login).toHaveBeenCalled()
  })
})
