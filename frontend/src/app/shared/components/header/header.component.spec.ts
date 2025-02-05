import {ComponentFixture, TestBed} from '@angular/core/testing'
import {HeaderComponent} from './header.component'
import {NavItemComponent} from './components/nav-item/nav-item.component'
import {UserMenuComponent} from './components/user-menu/user-menu.component'
import {provideRouter} from '@angular/router'
import {UserButtonComponent} from './components/user-button/user-button.component'

describe('HeaderComponent', () => {
  let component: HeaderComponent
  let fixture: ComponentFixture<HeaderComponent>

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [HeaderComponent, NavItemComponent, UserMenuComponent, UserButtonComponent],
      providers: [provideRouter([])]
    })
      .compileComponents()

    fixture = TestBed.createComponent(HeaderComponent)
    component = fixture.componentInstance
    fixture.detectChanges()
  })

  it('should create', () => {
    expect(component).toBeTruthy()
  })

  it('should default user to null', () => {
    expect(component.user()).toBeNull()
  })

  it('should show login and signup if user is null', () => {
    expect(component.user()).toBeNull()
    const compiled = fixture.nativeElement as HTMLElement
    expect(compiled.querySelector('wm-user-button[route="/login"]')?.textContent).toContain('Anmelden')
    expect(compiled.querySelector('wm-user-button[route="/sign-up"]')?.textContent).toContain('Registrieren')
  })

  it('should not show dashboard if user is null', () => {
    expect(component.user()).toBeNull()
    const compiled = fixture.nativeElement as HTMLElement
    expect(compiled.querySelector('nav')?.textContent).not.toContain('Dashboard')
  })

  it('should show home if user is null', () => {
    expect(component.user()).toBeNull()
    const compiled = fixture.nativeElement as HTMLElement
    expect(compiled.querySelector('wm-nav-item')?.textContent).toContain('Home')
  })
})
