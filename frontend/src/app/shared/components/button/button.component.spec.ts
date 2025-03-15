import { ComponentFixture, TestBed } from '@angular/core/testing'

import { ButtonComponent } from './button.component'

describe('ButtonComponent', () => {
  let component: ButtonComponent
  let fixture: ComponentFixture<ButtonComponent>

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [ButtonComponent]
    })
    .compileComponents()

    fixture = TestBed.createComponent(ButtonComponent)
    component = fixture.componentInstance
    fixture.detectChanges()
  })

  it('should create', () => {
    expect(component).toBeTruthy()
  })

  it('should have toggle for secondary design', () => {
    expect(component.secondary).toBeTruthy()
  })

  it('should map \'\' to true', () => {
    expect(component.secondary()).toBeFalse()
    fixture.componentRef.setInput('secondary', '')
    expect(component.secondary()).toBeTrue()
  })

  it('should map false to false', () => {
    fixture.componentRef.setInput('secondary', false)
    expect(component.secondary()).toBeFalse()
  })

  it('should map true to true', () => {
    fixture.componentRef.setInput('secondary', true)
    expect(component.secondary()).toBeTrue()
  })

  it('should have output for click event', () => {
    expect(component.clickEvent).toBeTruthy()
  })

  it('should contain a button', () => {
    expect(fixture.nativeElement.querySelector('button')).toBeTruthy()
  })

  it('should emit click', () => {
    const spy = spyOn(component.clickEvent, 'emit')
    component.click(new Event('click'))
    expect(spy).toHaveBeenCalled()
  })

  it('should emit click event on button click', () => {
    const spy = spyOn(component.clickEvent, 'emit')
    fixture.nativeElement.querySelector('button').click()
    expect(spy).toHaveBeenCalled()
  })

  it('should have an input for tab index', () => {
    expect(component.tabindex).toBeTruthy()
  })

  it('should have default tab index of 0', () => {
    expect(component.tabindex()).toBe(0)
    expect(fixture.nativeElement.querySelector('button').tabIndex).toBe(0)
  })

  it('should set tab index to 1', () => {
    fixture.componentRef.setInput('tabindex', 1)
    fixture.detectChanges()
    expect(component.tabindex()).toBe(1)
    expect(fixture.nativeElement.querySelector('button').tabIndex).toBe(1)
  })

  it('should stop propagation on click', () => {
    const spy = spyOn(Event.prototype, 'stopPropagation')
    component.click(new Event('click'))
    expect(spy).toHaveBeenCalled()
  })

  it('should prevent default on click', () => {
    const spy = spyOn(Event.prototype, 'preventDefault')
    component.click(new Event('click'))
    expect(spy).toHaveBeenCalled()
  })

  it('should have a default primary design', () => {
    expect(fixture.nativeElement.querySelector('button').classList).not.toContain('secondary')
  })

  it('should have a secondary design', () => {
    fixture.componentRef.setInput('secondary', true)
    fixture.detectChanges()
    expect(fixture.nativeElement.querySelector('button').classList).toContain('secondary')
  })

  it('should have an input for disabled', () => {
    expect(component.disabled).toBeTruthy()
  })

  it('should map \'\' to true', () => {
    expect(component.disabled()).toBeFalse()
    fixture.componentRef.setInput('disabled', '')
    expect(component.disabled()).toBeTrue()
  })

  it('should map false to false', () => {
    fixture.componentRef.setInput('disabled', false)
    expect(component.disabled()).toBeFalse()
  })

  it('should map true to true', () => {
    fixture.componentRef.setInput('disabled', true)
    expect(component.disabled()).toBeTrue()
  })

  it('should have a default enabled state', () => {
    expect(fixture.nativeElement.querySelector('button').disabled).toBeFalse()
  })

  it('should be disabled', () => {
    fixture.componentRef.setInput('disabled', true)
    fixture.detectChanges()
    expect(fixture.nativeElement.querySelector('button').disabled).toBeTrue()
  })
})
