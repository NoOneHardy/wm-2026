import {ComponentFixture, TestBed} from '@angular/core/testing'
import {HeaderComponent} from './header.component'
import {NavItemComponent} from './components/nav-item/nav-item.component'
import {UserMenuComponent} from './components/user-menu/user-menu.component'
import {provideRouter} from '@angular/router'
import {UserButtonComponent} from './components/user-button/user-button.component'
import {MockStore, provideMockStore} from '@ngrx/store/testing'
import {selectIsAdmin, selectUser} from '../../../user-management/store/user.feature'
import {Role} from '../../../model/user/role'

describe('HeaderComponent', () => {
  let component: HeaderComponent
  let fixture: ComponentFixture<HeaderComponent>
  let store: MockStore

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [HeaderComponent, NavItemComponent, UserMenuComponent, UserButtonComponent],
      providers: [provideRouter([]), provideMockStore()]
    }).compileComponents()

    store = TestBed.inject(MockStore)
    store.overrideSelector(selectUser, null)
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

  it('should show login and signup if user is null', () => {
    expect(component.user()).toBeNull()
    const compiled = fixture.nativeElement as HTMLElement
    expect(compiled.querySelector('wm-user-button[route="/login"]')?.textContent).toContain('Anmelden')
    expect(compiled.querySelector('wm-user-button[route="/signup"]')?.textContent).toContain('Registrieren')
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

  it('should load user from store', () => {
    expect(component.user()).toBeNull()

    store.overrideSelector(selectUser, {
      id: 'user-1',
      username: 'User',
      email: 'user@no1hardy.ch',
      firstname: 'Silas',
      lastname: 'No1hardy',
      isActive: true,
      role: Role.USER,
      points: 0,
      createdAt: new Date(),
      updatedAt: new Date()
    })
    store.refreshState()
    fixture.detectChanges()

    expect(component.user()?.id).toBe('user-1')
  })

  it('should load is admin from store', () => {
    expect(component.isAdmin()).toBeFalse()

    store.overrideSelector(selectIsAdmin, true)
    store.refreshState()
    fixture.detectChanges()
    expect(component.isAdmin()).toBe(true)
  })
})
