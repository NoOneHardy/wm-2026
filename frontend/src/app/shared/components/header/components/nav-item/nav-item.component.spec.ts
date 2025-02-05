import {ComponentFixture, TestBed} from '@angular/core/testing'
import {NavItemComponent} from './nav-item.component'
import {provideRouter, RouterLinkActive} from '@angular/router'

describe('NavItemComponent', () => {
  let component: NavItemComponent
  let fixture: ComponentFixture<NavItemComponent>

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [NavItemComponent, RouterLinkActive],
      providers: [provideRouter([])],
    })
      .compileComponents()

    fixture = TestBed.createComponent(NavItemComponent)
    component = fixture.componentInstance
    fixture.detectChanges()
  })

  it('should create', () => {
    expect(component).toBeTruthy()
  })

  it('should default icon to null', () => {
    expect(component.icon()).toBeNull()
  })

  it('should default displayName to null', () => {
    expect(component.displayName()).toBeNull()
  })

  it('should default displayName to /', () => {
    expect(component.route()).toBe('/')
  })

  it('should display green bar on active link', () => {
    const compiled = fixture.nativeElement as HTMLElement
    const item = compiled.querySelector('.nav-item')
    expect(item).toBeTruthy()
    if (!item) return
    item.classList.add('active')

    expect(getComputedStyle(item, ':after').width.slice(0, -2)).toBeGreaterThan(0)
  })

  it('should fill icon on active link', () => {
    fixture.componentRef.setInput('icon', 'home')
    fixture.detectChanges()

    const compiled = fixture.nativeElement as HTMLElement
    const item = compiled.querySelector('.nav-item')
    const icon = compiled.querySelector('.icon')
    expect(item).toBeTruthy()
    expect(icon).toBeTruthy()
    if (!item || !icon) return
    item.classList.add('active')

    expect(getComputedStyle(icon).fontVariationSettings).toContain('"FILL" 1')
  })

  it('should display displayName', () => {
    fixture.componentRef.setInput('displayName', 'Home')
    fixture.detectChanges()

    const compiled = fixture.nativeElement as HTMLElement
    expect(compiled.textContent).toContain('Home')
  })
})
