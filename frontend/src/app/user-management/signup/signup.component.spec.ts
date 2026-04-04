import {ComponentFixture, TestBed} from '@angular/core/testing'
import {SignupComponent} from './signup.component'
import {provideHttpClient} from '@angular/common/http'
import {provideMockStore} from '@ngrx/store/testing'
import {EffectRef, Injectable} from '@angular/core'
import {of} from 'rxjs'
import {AbstractControl, AsyncValidatorFn} from '@angular/forms'
import {UserValidatorService} from './validators/user-validator.service'


@Injectable({
  providedIn: 'root'
})
class MockUserValidatorService extends UserValidatorService {
  override usernameAvailable(): AsyncValidatorFn {
    return (control: AbstractControl) => {
      if (control.value === 'No1Hardy') return of({usernameAvailable: true})
      return of(null)
    }
  }

  override emailAvailable(): AsyncValidatorFn {
    return (control: AbstractControl) => {
      if (control.value === 'test@no1hardy.ch') return of({emailAvailable: true})
      return of(null)
    }
  }
}

describe('SignupComponent', () => {
  let component: SignupComponent
  let fixture: ComponentFixture<SignupComponent>

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [SignupComponent],
      providers: [provideHttpClient(), provideMockStore(), {
        provide: UserValidatorService,
        useExisting: MockUserValidatorService
      }]
    }).compileComponents()

    fixture = TestBed.createComponent(SignupComponent)
    component = fixture.componentInstance
    fixture.detectChanges()
  })

  it('should create', () => {
    expect(component).toBeTruthy()
  })

  it('should have a form group', () => {
    expect(component.formGroup).toBeTruthy()
    expect(component.formGroup.get('username')).toBeTruthy()
    expect(component.formGroup.get('email')).toBeTruthy()
    expect(component.formGroup.get('firstname')).toBeTruthy()
    expect(component.formGroup.get('lastname')).toBeTruthy()
    expect(component.formGroup.get('passwords')).toBeTruthy()
    expect(component.formGroup.get('passwords')?.get('password')).toBeTruthy()
    expect(component.formGroup.get('passwords')?.get('confirmPassword')).toBeTruthy()
  })

  it('should validate username', () => {
    const usernameControl = component.formGroup.get('username')
    expect(usernameControl).toBeTruthy()
    if (!usernameControl) return

    expect(usernameControl.value).toBe('')
    expect(usernameControl.errors?.['required']).toBeTruthy()
    usernameControl.setValue('123')
    expect(usernameControl.errors?.['required']).toBeFalsy()
    expect(usernameControl.errors?.['minlength']).toBeTruthy()
    usernameControl.setValue('No1Hardy')
    expect(usernameControl.errors?.['minlength']).toBeFalsy()
    expect(usernameControl.errors?.['usernameAvailable']).toBeTruthy()
    usernameControl.setValue('NoOneHardy')
    expect(usernameControl.errors?.['usernameAvailable']).toBeFalsy()
    expect(usernameControl.valid).toBeTrue()
  })

  it('should validate email', () => {
    const emailControl = component.formGroup.get('email')
    expect(emailControl).toBeTruthy()
    if (!emailControl) return

    expect(emailControl.value).toBe('')
    expect(emailControl.errors?.['required']).toBeTruthy()
    emailControl.setValue('test.ch')
    expect(emailControl.errors?.['required']).toBeFalsy()
    expect(emailControl.errors?.['email']).toBeTruthy()
    emailControl.setValue('test@no1hardy.ch')
    expect(emailControl.errors?.['email']).toBeFalsy()
    expect(emailControl.errors?.['emailAvailable']).toBeTruthy()
    emailControl.setValue('admin@no1hardy.ch')
    expect(emailControl.errors?.['emailAvailable']).toBeFalsy()
    expect(emailControl.valid).toBeTrue()
  })

  it('should validate firstname', () => {
    const firstnameControl = component.formGroup.get('firstname')
    expect(firstnameControl).toBeTruthy()
    if (!firstnameControl) return

    expect(firstnameControl.value).toBe('')
    expect(firstnameControl.errors?.['required']).toBeTruthy()
    firstnameControl.setValue('S')
    expect(firstnameControl.errors?.['required']).toBeFalsy()
    expect(firstnameControl.errors?.['minlength']).toBeTruthy()
    firstnameControl.setValue('Silas')
    expect(firstnameControl.errors?.['minlength']).toBeFalsy()
    expect(firstnameControl.valid).toBeTrue()
  })

  it('should validate lastname', () => {
    const lastnameControl = component.formGroup.get('lastname')
    expect(lastnameControl).toBeTruthy()
    if (!lastnameControl) return

    expect(lastnameControl.value).toBe('')
    expect(lastnameControl.errors?.['required']).toBeTruthy()
    lastnameControl.setValue('H')
    expect(lastnameControl.errors?.['required']).toBeFalsy()
    expect(lastnameControl.errors?.['minlength']).toBeTruthy()
    lastnameControl.setValue('Hardegger')
    expect(lastnameControl.errors?.['minlength']).toBeFalsy()
    expect(lastnameControl.valid).toBeTrue()
  })

  it('should validate password fields', () => {
    const controls = ['password', 'confirmPassword']
    controls.forEach((control) => {
      const passwordControl = component.formGroup.get('passwords')?.get(control)
      expect(passwordControl).toBeTruthy()
      if (!passwordControl) return

      expect(passwordControl.value).toBe('')
      expect(passwordControl.errors?.['required']).toBeTruthy()
      passwordControl.setValue('pass')
      expect(passwordControl.errors?.['required']).toBeFalsy()
      expect(passwordControl.errors?.['minlength']).toBeTruthy()
      passwordControl.setValue('password')
      expect(passwordControl.errors?.['minlength']).toBeFalsy()
      expect(passwordControl.valid).toBeTrue()

    })
  })

  it('should check whether passwords match', () => {
    const passwordGroup = component.formGroup.get('passwords')
    const passwordControl = passwordGroup?.get('password')
    const confirmControl = passwordGroup?.get('confirmPassword')

    expect(passwordGroup).toBeTruthy()
    expect(passwordControl).toBeTruthy()
    expect(confirmControl).toBeTruthy()
    if (!passwordGroup || !passwordControl || !confirmControl) return

    expect(passwordControl.value).toBe('')
    expect(confirmControl.value).toBe('')
    expect(passwordGroup.errors?.['passwordMatch']).toBeFalsy()
    passwordControl.setValue('password1')
    confirmControl.setValue('password2')
    expect(passwordGroup.errors?.['passwordMatch']).toBeTruthy()
    passwordControl.setValue('password')
    confirmControl.setValue('password')
    expect(passwordGroup.valid).toBeTrue()
  })

  it('should dispatch user create action', () => {
    const spy = spyOn(component['store'], 'dispatch').and.callFake(() => {
      return null as unknown as EffectRef
    })
    component.formGroup.setValue({
      username: 'NoOneHardy',
      email: 'admin@no1hardy.ch',
      firstname: 'Silas',
      lastname: 'Hardegger',
      passwords: {
        password: 'password',
        confirmPassword: 'password'
      }
    })
    component.submit()
    expect(spy).toHaveBeenCalled()
  })
})
