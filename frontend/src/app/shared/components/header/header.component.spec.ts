import {ComponentFixture, TestBed} from '@angular/core/testing'
import {vi} from 'vitest'
import {HeaderComponent} from './header.component'
import {NavItemComponent} from './components/nav-item/nav-item.component'
import {UserMenuComponent} from './components/user-menu/user-menu.component'
import {provideRouter} from '@angular/router'
import {UserButtonComponent} from './components/user-button/user-button.component'
import {MockStore, provideMockStore} from '@ngrx/store/testing'
import {selectIsAdmin, selectNotifications, selectUser} from '../../../user-management/store/user.feature'
import {Role} from '../../../model/user/role'
import {UserApplicationStatus} from '../../../model/user/user-application-status'
import {logout} from '../../../user-management/store/user.actions'
import {provideAnimations} from '@angular/platform-browser/animations'
import {ViewportService} from '../../services/viewport/viewport.service'

describe('HeaderComponent', () => {
  let component: HeaderComponent
  let fixture: ComponentFixture<HeaderComponent>
  let store: MockStore
  let viewportService: ViewportService

  const user = {
    id: 'user-1',
    username: 'User',
    email: 'user@no1hardy.ch',
    firstname: 'Silas',
    lastname: 'No1hardy',
    role: Role.USER,
    points: 0,
    lastReviewedPoints: 0,
    userApplicationStatus: UserApplicationStatus.PENDING
  }

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [HeaderComponent, NavItemComponent, UserMenuComponent, UserButtonComponent],
      providers: [provideRouter([]), provideMockStore(), provideAnimations()]
    }).compileComponents()

    store = TestBed.inject(MockStore)
    viewportService = TestBed.inject(ViewportService)
    store.overrideSelector(selectUser, null)
    store.overrideSelector(selectNotifications, [])
    store.refreshState()

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

  it('should load user from store', () => {
    expect(component.user()).toBeNull()

    store.overrideSelector(selectUser, user)
    store.refreshState()
    fixture.detectChanges()

    expect(component.user()?.id).toBe('user-1')
  })

  it('should load is admin from store', () => {
    store.overrideSelector(selectIsAdmin, false)
    store.refreshState()
    fixture.detectChanges()
    expect(component.isAdmin()).toBe(false)

    store.overrideSelector(selectIsAdmin, true)
    store.refreshState()
    fixture.detectChanges()
    expect(component.isAdmin()).toBe(true)
  })

  it('should dispatch logout action when logout is called', () => {
    const storeSpy = vi.spyOn(component['store'], 'dispatch')

    component.logout()

    expect(storeSpy).toHaveBeenCalledWith(logout())
  })

  it('should toggle menu state when toggleMenu is called', () => {
    component.isExpanded = false
    component.toggleMenu()
    expect(component.isExpanded).toBe(true)
    component.toggleMenu()
    expect(component.isExpanded).toBe(false)
  })

  it('should close menu on blur if click is outside the component', () => {
    const event = new MouseEvent('click')
    vi.spyOn(event, 'target', 'get').mockReturnValue(document.createElement('div'))

    component.isExpanded = true
    component.closeMenuOnBlur(event)

    expect(component.isExpanded).toBe(false)
  })

  it('should not close menu on blur if click is inside the component', () => {
    const event = new MouseEvent('click')
    vi.spyOn(event, 'target', 'get').mockReturnValue(fixture.nativeElement)

    component.isExpanded = true
    component.closeMenuOnBlur(event)

    expect(component.isExpanded).toBe(true)
  })

  it('should show the menu button on mobile for guests', () => {
    viewportService.viewportWidth.set(480)
    fixture.detectChanges()

    expect(fixture.nativeElement.querySelector('.menu-button')).toBeTruthy()
  })

  it('should render guest navigation and auth links in the mobile menu', () => {
    viewportService.viewportWidth.set(480)
    component.isExpanded = true
    fixture.detectChanges()

    const mobileMenu = fixture.nativeElement.querySelector('#mobile-menu') as HTMLElement | null
    expect(mobileMenu?.textContent).toContain('Home')
    expect(mobileMenu?.textContent).toContain('Rangliste')
    expect(mobileMenu?.textContent).toContain('Anmelden')
    expect(mobileMenu?.textContent).toContain('Registrieren')
  })

  it('should show the account menu button on mobile for logged in users', () => {
    store.overrideSelector(selectUser, user)
    store.refreshState()
    viewportService.viewportWidth.set(480)
    fixture.detectChanges()

    expect(fixture.nativeElement.querySelector('.menu-button')).toBeTruthy()
  })

  it('should render admin actions in the mobile menu', () => {
    store.overrideSelector(selectUser, user)
    store.overrideSelector(selectIsAdmin, true)
    store.refreshState()
    viewportService.viewportWidth.set(480)
    component.isExpanded = true
    fixture.detectChanges()

    const mobileMenu = fixture.nativeElement.querySelector('#mobile-menu') as HTMLElement | null
    expect(mobileMenu).toBeTruthy()
    expect(mobileMenu?.textContent).toContain('Teams')
    expect(mobileMenu?.textContent).toContain('Gruppen')
    expect(mobileMenu?.textContent).toContain('Spiele')
    expect(mobileMenu?.textContent).toContain('Resultate')
    expect(mobileMenu?.textContent).toContain('Teilnehmer')
    expect(mobileMenu?.textContent).toContain('Abmelden')
  })
})
