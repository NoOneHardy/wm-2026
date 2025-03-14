import {ComponentFixture} from '@angular/core/testing'

import {FormFieldComponent} from './form-field.component'
import {configureTestHost, TestHostComponent} from '../test-host/test-host.component'

describe('FormFieldComponent', () => {
  let component: TestHostComponent
  let fixture: ComponentFixture<TestHostComponent>

  beforeEach(async () => {
    [fixture, component] = await configureTestHost({
      imports: [FormFieldComponent],
      template: '<wm-form-field><input></wm-form-field>'
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
      template: '<wm-form-field><input type="password"></wm-form-field>'
    })

    expect(fixture.nativeElement.querySelector('button.show-password-icon')).toBeTruthy()
  })
})
