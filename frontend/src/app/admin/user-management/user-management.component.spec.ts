import {ComponentFixture, TestBed} from '@angular/core/testing'
import {MockStore, provideMockStore} from '@ngrx/store/testing'
import {UserManagementComponent} from './user-management.component'
import {selectUsers} from '../store/admin.feature'
import {mockUser1} from '../../model/mock/user.mock'
import {loadUsers} from '../store/admin.actions'
import {User} from '../../model/user/user'
import {selectUser} from '../../user-management/store/user.feature'
import {UserApplicationStatus} from '../../model/user/user-application-status'

describe('UserManagementComponent', () => {
  let component: UserManagementComponent
  let fixture: ComponentFixture<UserManagementComponent>
  let store: MockStore
  let mockUser: User

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [UserManagementComponent],
      providers: [provideMockStore()]
    }).compileComponents()

    fixture = TestBed.createComponent(UserManagementComponent)
    component = fixture.componentInstance
    store = TestBed.inject(MockStore)
    mockUser = {...mockUser1}

    store.overrideSelector(selectUsers, [mockUser])
    fixture.detectChanges()
  })

  it('should create', () => {
    expect(component).toBeTruthy()
  })

  it('should load users from store', () => {
    expect(component._users()).toEqual([mockUser])
  })

  it('should load users on init', () => {
    spyOn(store, 'dispatch')
    component.ngOnInit()
    expect(store.dispatch).toHaveBeenCalledWith(loadUsers())
  })

  it('should filter current user from the list', () => {
    const currentUser = {
      ...mockUser1,
      id: 'current-user-id'
    }
    store.overrideSelector(selectUsers, [currentUser, mockUser])
    store.overrideSelector(selectUser, currentUser)
    store.refreshState()
    fixture.detectChanges()

    expect(component.users()).toEqual([mockUser])
  })

  it('should load current user from store', () => {
    store.overrideSelector(selectUser, mockUser)
    store.refreshState()
    fixture.detectChanges()

    expect(component.currentUser()).toEqual(mockUser)
  })

  it('should evaluate whether the user is accepted', () => {
    mockUser.userApplicationStatus = UserApplicationStatus.ACCEPTED
    mockUser.applicationReviewedAt = new Date('2025-06-19T17:04:00')

    expect(component.isAccepted(mockUser)).toBeTrue()

    mockUser.userApplicationStatus = UserApplicationStatus.PENDING
    expect(component.isAccepted(mockUser)).toBeFalse()

    mockUser.userApplicationStatus = UserApplicationStatus.DENIED
    expect(component.isAccepted(mockUser)).toBeFalse()

    mockUser.userApplicationStatus = UserApplicationStatus.ACCEPTED
    mockUser.applicationReviewedAt = null
    expect(component.isAccepted(mockUser)).toBeFalse()
  })

  it('should evaluate whether the user is denied', () => {
    mockUser.userApplicationStatus = UserApplicationStatus.DENIED
    mockUser.applicationReviewedAt = new Date('2025-06-19T17:04:00')

    expect(component.isDenied(mockUser)).toBeTrue()

    mockUser.userApplicationStatus = UserApplicationStatus.PENDING
    expect(component.isDenied(mockUser)).toBeFalse()

    mockUser.userApplicationStatus = UserApplicationStatus.ACCEPTED
    expect(component.isDenied(mockUser)).toBeFalse()

    mockUser.userApplicationStatus = UserApplicationStatus.DENIED
    mockUser.applicationReviewedAt = null
    expect(component.isDenied(mockUser)).toBeFalse()
  })
})
