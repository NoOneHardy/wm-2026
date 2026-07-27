import {ComponentFixture} from '@angular/core/testing'

import {FormFieldComponent} from './form-field.component'
import {configureTestHost, TestHostComponent} from '../test-host/test-host.component'

describe('FormFieldComponent', () => {
  let component: TestHostComponent
  let fixture: ComponentFixture<TestHostComponent>

  beforeEach(async () => {
    [fixture, component] = await configureTestHost({
      imports: [FormFieldComponent],
      template: '<bet-form-field><input></bet-form-field>'
    })
  })

  it('should create', () => {
    expect(component).toBeTruthy()
  })

  it('should set placeholder to ""', () => {
    const input = fixture.nativeElement.querySelector('input')
    expect(input.placeholder).toBe('')
  })

  it('should set isPassword to true', async () => {
    [fixture, component] = await configureTestHost({
      imports: [FormFieldComponent],
      template: '<bet-form-field><input type="password"></bet-form-field>'
    })

    expect(fixture.nativeElement.querySelector('button.show-password-icon')).toBeTruthy()
  })

  it('should toggle password visibility', async () => {
    [fixture, component] = await configureTestHost({
      imports: [FormFieldComponent],
      template: '<bet-form-field><input type="password"></bet-form-field>'
    })

    expect(fixture.nativeElement.querySelector('button.show-password-icon')).toBeTruthy()
    fixture.nativeElement.querySelector('button.show-password-icon').click()
    fixture.detectChanges()
    expect(fixture.nativeElement.querySelector('input').type).toBe('text')
  })
})
