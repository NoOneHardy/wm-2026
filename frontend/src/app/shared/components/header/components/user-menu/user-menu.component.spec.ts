import {ComponentFixture, TestBed} from '@angular/core/testing'

import {UserMenuComponent} from './user-menu.component'
import {User} from '../../../../../model/user/user'
import {MockStore, provideMockStore} from '@ngrx/store/testing'
import {mockUser1} from '../../../../../model/mock/user.mock'
import {selectNotifications} from '../../../../../user-management/store/user.feature'

describe('UserMenuComponent', () => {
  let component: UserMenuComponent
  let fixture: ComponentFixture<UserMenuComponent>
  let user: User
  let store: MockStore

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [UserMenuComponent],
      providers: [provideMockStore()]
    }).compileComponents()

    fixture = TestBed.createComponent(UserMenuComponent)
    store = TestBed.inject(MockStore)
    store.overrideSelector(selectNotifications, [])
    component = fixture.componentInstance
    fixture.detectChanges()
    user = {...mockUser1}
  })

  it('should create', () => {
    expect(component).toBeTruthy()
  })

  it('should toggle dropdown visibility', () => {
    expect(component.isDropdownExpanded()).toBe(false)
    component.toggleDropdown()
    expect(component.isDropdownExpanded()).toBe(true)
    component.toggleDropdown()
    expect(component.isDropdownExpanded()).toBe(false)
  })

  it('should not display anything if user is null', () => {
    expect(fixture.nativeElement.querySelector('*')).toBeNull()
  })

  it('should display user name if user is not null', () => {
    fixture.componentRef.setInput('user', user)
    fixture.detectChanges()
    expect(fixture.nativeElement.querySelector('bet-user-button')?.textContent).toContain('User1')
  })
})
