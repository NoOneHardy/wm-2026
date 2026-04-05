import {Component} from '@angular/core'
import {ComponentFixture, TestBed} from '@angular/core/testing'
import {By} from '@angular/platform-browser'
import {MatFormFieldModule} from '@angular/material/form-field'
import {MatIcon} from '@angular/material/icon'
import {MatInput, MatSuffix} from '@angular/material/input'

import {PasswordIconDirective} from './password-icon.directive'

@Component({
  imports: [
    MatFormFieldModule,
    MatInput,
    MatSuffix,
    MatIcon,
    PasswordIconDirective
  ],
  template: `
    <mat-form-field>
      <input matInput type="password">
      <mat-icon matSuffix wmPasswordIcon></mat-icon>
    </mat-form-field>
  `
})
class HostComponent {
}

describe('PasswordIconDirective', () => {
  let fixture: ComponentFixture<HostComponent>

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [HostComponent],
      providers: []
    }).compileComponents()

    fixture = TestBed.createComponent(HostComponent)
    fixture.detectChanges()
  })

  function getDirective(): PasswordIconDirective {
    return fixture.debugElement.query(By.directive(PasswordIconDirective)).injector.get(PasswordIconDirective)
  }

  function getInput(): HTMLInputElement {
    return fixture.nativeElement.querySelector('input')
  }

  function getIcon(): HTMLElement {
    return fixture.nativeElement.querySelector('mat-icon')
  }

  it('should initialize with a hidden password input', () => {
    const directive = getDirective()

    expect(directive).toBeTruthy()
    expect(directive['input']()).toBe(getInput())
    expect(directive.isVisible()).toBeFalse()
    expect(getInput().type).toBe('password')
    expect(getIcon().textContent?.trim()).toBe('visibility')
  })

  it('should toggle the password visibility on pointerup', () => {
    const icon = getIcon()

    icon.dispatchEvent(new Event('pointerup'))
    fixture.detectChanges()

    expect(getDirective().isVisible()).toBeTrue()
    expect(getInput().type).toBe('text')
    expect(icon.textContent?.trim()).toBe('visibility_off')

    icon.dispatchEvent(new Event('pointerup'))
    fixture.detectChanges()

    expect(getDirective().isVisible()).toBeFalse()
    expect(getInput().type).toBe('password')
    expect(icon.textContent?.trim()).toBe('visibility')
  })

  it('should expose pointer styling on the host', () => {
    const icon = getIcon()
    expect(icon.style.cursor).toBe('pointer')
  })
})
