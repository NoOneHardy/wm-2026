import {ComponentFixture, TestBed} from '@angular/core/testing'
import {HeaderComponent} from './header.component'
import {NavItemComponent} from './components/nav-item/nav-item.component'
import {UserMenuComponent} from './components/user-menu/user-menu.component'
import {provideRouter} from '@angular/router'
import {UserButtonComponent} from './components/user-button/user-button.component'
import {MockStore, provideMockStore} from '@ngrx/store/testing'
import {selectIsAdmin, selectUser} from '../../../user-management/store/user.feature'
import {Role} from '../../../model/user/role'
import {UserApplicationStatus} from '../../../model/user/user-application-status'
import {logout} from '../../../user-management/store/user.actions'
import {provideAnimations} from '@angular/platform-browser/animations'

describe('HeaderComponent', () => {
  let component: HeaderComponent
  let fixture: ComponentFixture<HeaderComponent>
  let store: MockStore

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [HeaderComponent, NavItemComponent, UserMenuComponent, UserButtonComponent],
      providers: [provideRouter([]), provideMockStore(), provideAnimations()]
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

  it('should load user from store', () => {
    expect(component.user()).toBeNull()

    store.overrideSelector(selectUser, {
      id: 'user-1',
      username: 'User',
      email: 'user@no1hardy.ch',
      firstname: 'Silas',
      lastname: 'No1hardy',
      role: Role.USER,
      points: 0,
      lastReviewedPoints: 0,
      userApplicationStatus: UserApplicationStatus.PENDING
    })
    store.refreshState()
    fixture.detectChanges()

    expect(component.user()?.id).toBe('user-1')
  })

  it('should load is admin from store', () => {
    store.overrideSelector(selectIsAdmin, false)
    store.refreshState()
    fixture.detectChanges()
    expect(component.isAdmin()).toBeFalse()

    store.overrideSelector(selectIsAdmin, true)
    store.refreshState()
    fixture.detectChanges()
    expect(component.isAdmin()).toBe(true)
  })

  it('should dispatch logout action when logout is called', () => {
    const storeSpy = spyOn(component['store'], 'dispatch').and.callThrough()

    component.logout()

    expect(storeSpy).toHaveBeenCalledWith(logout())
  })

  it('should toggle menu state when toggleMenu is called', () => {
    component.isExpanded = false
    component.toggleMenu()
    expect(component.isExpanded).toBeTrue()
    component.toggleMenu()
    expect(component.isExpanded).toBeFalse()
  })

  it('should close menu on blur if click is outside the component', () => {
    const event = new MouseEvent('click')
    spyOnProperty(event, 'target').and.returnValue(document.createElement('div'))

    component.isExpanded = true
    component.closeMenuOnBlur(event)

    expect(component.isExpanded).toBeFalse()
  })

  it('should not close menu on blur if click is inside the component', () => {
    const event = new MouseEvent('click')
    spyOnProperty(event, 'target').and.returnValue(fixture.nativeElement)

    component.isExpanded = true
    component.closeMenuOnBlur(event)

    expect(component.isExpanded).toBeTrue()
  })
})
