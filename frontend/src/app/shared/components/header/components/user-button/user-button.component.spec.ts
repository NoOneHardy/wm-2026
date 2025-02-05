import { ComponentFixture, TestBed } from '@angular/core/testing'
import { UserButtonComponent } from './user-button.component'
import {provideRouter} from '@angular/router'

describe('UserButtonComponent', () => {
  let component: UserButtonComponent
  let fixture: ComponentFixture<UserButtonComponent>

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [UserButtonComponent],
      providers: [provideRouter([])]
    })
    .compileComponents()

    fixture = TestBed.createComponent(UserButtonComponent)
    component = fixture.componentInstance
    fixture.detectChanges()
  })

  it('should create', () => {
    expect(component).toBeTruthy()
  })

  it('should emit on click no matter if it\'s a button or a link', () => {
    const clickSpy = spyOn(component.clickButton, 'emit')
    expect(clickSpy).not.toHaveBeenCalled()

    // Button
    fixture.nativeElement.querySelector('.button')?.click()
    expect(clickSpy).toHaveBeenCalled()

    // Link
    fixture.componentRef.setInput('route', '/')
    fixture.detectChanges()
    fixture.nativeElement.querySelector('.button')?.click()
    expect(clickSpy).toHaveBeenCalledTimes(2)
  })

  it('should be a link if a route is given', () => {
    fixture.componentRef.setInput('route', '/')
    fixture.detectChanges()
    expect(fixture.nativeElement.querySelector('.button')).toBeInstanceOf(HTMLAnchorElement)
  })

  it('should be a button if no route is given', () => {
    expect(fixture.nativeElement.querySelector('.button')).toBeInstanceOf(HTMLButtonElement)
  })

  it('should set innerHTML via content input', () => {
    fixture.componentRef.setInput('content', 'Hello World!')
    fixture.detectChanges()

    // Button
    expect(fixture.nativeElement.querySelector('.button')?.innerHTML).toBe('Hello World!')

    // Link
    fixture.componentRef.setInput('route', '/')
    fixture.detectChanges()
    expect(fixture.nativeElement.querySelector('.button')?.innerHTML).toBe('Hello World!')
  })

  it('should have class secondary if empty string is given', () => {
    fixture.componentRef.setInput('secondary', '')
    fixture.detectChanges()

    // Button
    expect(fixture.nativeElement.querySelector('.button')?.classList).toContain('secondary')

    // Link
    fixture.componentRef.setInput('route', '/')
    fixture.detectChanges()
    expect(fixture.nativeElement.querySelector('.button')?.classList).toContain('secondary')
  })

  it('should have class secondary if true is given', () => {
    fixture.componentRef.setInput('secondary', true)
    fixture.detectChanges()

    // Button
    expect(fixture.nativeElement.querySelector('.button')?.classList).toContain('secondary')

    // Link
    fixture.componentRef.setInput('route', '/')
    fixture.detectChanges()
    expect(fixture.nativeElement.querySelector('.button')?.classList).toContain('secondary')
  })
})
