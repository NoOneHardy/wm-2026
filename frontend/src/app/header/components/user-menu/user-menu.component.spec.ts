import { ComponentFixture, TestBed } from '@angular/core/testing'

import { UserMenuComponent } from './user-menu.component'
import {User} from '../../../model/user/user'

describe('UserMenuComponent', () => {
  let component: UserMenuComponent
  let fixture: ComponentFixture<UserMenuComponent>
  let user: User

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [UserMenuComponent]
    })
    .compileComponents()

    fixture = TestBed.createComponent(UserMenuComponent)
    component = fixture.componentInstance
    fixture.detectChanges()
    user = {
      id: 0,
      username: 'NoOneHardy',
      email: 'silas.hardegger@outlook.com',
      firstname: 'Silas',
      lastname: 'Hardegger',
      createdAt: new Date(),
      isActive: true,
      points: 0,
      updatedAt: new Date(),
      confirmedAt: new Date()
    }
  })

  it('should create', () => {
    expect(component).toBeTruthy()
  })

  it('should toggle dropdown visibility', () => {
    expect(component.isDropdownExpanded()).toBeFalse()
    component.toggleDropdown()
    expect(component.isDropdownExpanded()).toBeTrue()
    component.toggleDropdown()
    expect(component.isDropdownExpanded()).toBeFalse()
  })

  it('should not display anything if user is null', () => {
    expect(fixture.nativeElement.querySelector('*')).toBeNull()
  })

  it('should display user name if user is not null', () => {
    fixture.componentRef.setInput('user', user)
    fixture.detectChanges()
    expect(fixture.nativeElement.querySelector('wm-user-button')?.innerText).toContain('NoOneHardy')
  })
})
