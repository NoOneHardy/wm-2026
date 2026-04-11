import {ComponentFixture, TestBed} from '@angular/core/testing'
import {MockStore, provideMockStore} from '@ngrx/store/testing'
import {UserManagementComponent} from './user-management.component'
import {selectUsers} from '../store/admin.feature'
import {mockUser1} from '../../model/mock/user.mock'
import {confirmUser, denyUser, loadUsers} from '../store/admin.actions'
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
    const currentUser: User = {
      ...mockUser1,
      id: 'current-user-id'
    }
    store.overrideSelector(selectUsers, [currentUser, mockUser])
    store.overrideSelector(selectUser, currentUser)
    component.formGroup.patchValue({quickFilter: []})
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

  it('should sort users by application status and username', () => {
    store.overrideSelector(selectUser, null)
    store.overrideSelector(selectUsers, [
      {
        ...mockUser,
        id: 'user-1',
        username: 'Peter',
        userApplicationStatus: UserApplicationStatus.ACCEPTED,
        applicationReviewedAt: new Date()
      },
      {
        ...mockUser,
        id: 'user-2',
        username: 'Anna',
        userApplicationStatus: UserApplicationStatus.DENIED,
        applicationReviewedAt: new Date()
      },
      {
        ...mockUser,
        id: 'user-3',
        username: 'John',
        userApplicationStatus: UserApplicationStatus.PENDING,
        applicationReviewedAt: new Date()
      },
      {
        ...mockUser,
        id: 'user-4',
        username: 'Zoe',
        userApplicationStatus: UserApplicationStatus.PENDING,
        applicationReviewedAt: new Date()
      },
      {
        ...mockUser,
        id: 'user-5',
        username: 'Alice',
        userApplicationStatus: UserApplicationStatus.ACCEPTED,
        applicationReviewedAt: new Date()
      },
      {
        ...mockUser,
        id: 'user-6',
        username: 'Bob',
        userApplicationStatus: UserApplicationStatus.DENIED,
        applicationReviewedAt: new Date()
      }
    ])
    store.refreshState()
    component.formGroup.patchValue({quickFilter: []})
    fixture.detectChanges()

    expect(component.users().length).toBe(6)
    expect(component.users()[0].id).toBe('user-3')
    expect(component.users()[1].id).toBe('user-4')
    expect(component.users()[2].id).toBe('user-2')
    expect(component.users()[3].id).toBe('user-6')
    expect(component.users()[4].id).toBe('user-5')
    expect(component.users()[5].id).toBe('user-1')
  })

  it('should call confirmUser action on accept', () => {
    spyOn(store, 'dispatch')
    const userId = 'test-user-id'
    component.accept(userId)
    expect(store.dispatch).toHaveBeenCalledWith(confirmUser({id: userId}))
  })

  it('should call denyUser action on deny', () => {
    spyOn(store, 'dispatch')
    const userId = 'test-user-id'
    component.deny(userId)
    expect(store.dispatch).toHaveBeenCalledWith(denyUser({id: userId}))
  })
})
