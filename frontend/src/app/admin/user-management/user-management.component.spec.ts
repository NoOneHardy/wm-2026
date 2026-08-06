import {ComponentFixture, TestBed} from '@angular/core/testing'
import {vi} from 'vitest'
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
    vi.spyOn(store, 'dispatch')
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

    expect(component.isAccepted(mockUser)).toBe(true)

    mockUser.userApplicationStatus = UserApplicationStatus.PENDING
    expect(component.isAccepted(mockUser)).toBe(false)

    mockUser.userApplicationStatus = UserApplicationStatus.DENIED
    expect(component.isAccepted(mockUser)).toBe(false)

    mockUser.userApplicationStatus = UserApplicationStatus.ACCEPTED
    mockUser.applicationReviewedAt = null
    expect(component.isAccepted(mockUser)).toBe(false)
  })

  it('should evaluate whether the user is denied', () => {
    mockUser.userApplicationStatus = UserApplicationStatus.DENIED
    mockUser.applicationReviewedAt = new Date('2025-06-19T17:04:00')

    expect(component.isDenied(mockUser)).toBe(true)

    mockUser.userApplicationStatus = UserApplicationStatus.PENDING
    expect(component.isDenied(mockUser)).toBe(false)

    mockUser.userApplicationStatus = UserApplicationStatus.ACCEPTED
    expect(component.isDenied(mockUser)).toBe(false)

    mockUser.userApplicationStatus = UserApplicationStatus.DENIED
    mockUser.applicationReviewedAt = null
    expect(component.isDenied(mockUser)).toBe(false)
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

  it('should default to pending users in the quick filter', () => {
    store.overrideSelector(selectUser, null)
    store.overrideSelector(selectUsers, [
      {
        ...mockUser,
        id: 'pending-user',
        username: 'Pending User',
        userApplicationStatus: UserApplicationStatus.PENDING,
        applicationReviewedAt: null
      },
      {
        ...mockUser,
        id: 'accepted-user',
        username: 'Accepted User',
        userApplicationStatus: UserApplicationStatus.ACCEPTED,
        applicationReviewedAt: new Date()
      },
      {
        ...mockUser,
        id: 'denied-user',
        username: 'Denied User',
        userApplicationStatus: UserApplicationStatus.DENIED,
        applicationReviewedAt: new Date()
      }
    ])
    store.refreshState()
    fixture.detectChanges()

    expect(component.formGroup.controls.quickFilter.getRawValue()).toEqual([UserApplicationStatus.PENDING])
    expect(component.users().map(user => user.id)).toEqual(['pending-user'])
  })

  it('should filter users by the selected application statuses', () => {
    store.overrideSelector(selectUser, null)
    store.overrideSelector(selectUsers, [
      {
        ...mockUser,
        id: 'pending-user',
        username: 'Pending User',
        userApplicationStatus: UserApplicationStatus.PENDING,
        applicationReviewedAt: null
      },
      {
        ...mockUser,
        id: 'accepted-user',
        username: 'Accepted User',
        userApplicationStatus: UserApplicationStatus.ACCEPTED,
        applicationReviewedAt: new Date()
      },
      {
        ...mockUser,
        id: 'denied-user',
        username: 'Denied User',
        userApplicationStatus: UserApplicationStatus.DENIED,
        applicationReviewedAt: new Date()
      }
    ])
    store.refreshState()
    component.formGroup.patchValue({
      quickFilter: [UserApplicationStatus.ACCEPTED, UserApplicationStatus.DENIED]
    })
    fixture.detectChanges()

    expect(component.users().map(user => user.id)).toEqual(['denied-user', 'accepted-user'])
  })

  it('should search users by username, email, firstname and lastname case-insensitively', () => {
    store.overrideSelector(selectUser, null)
    store.overrideSelector(selectUsers, [
      {
        ...mockUser,
        id: 'matching-user',
        username: 'CaptainDemo',
        email: 'captain@example.com',
        firstname: 'Alex',
        lastname: 'Anderson',
        userApplicationStatus: UserApplicationStatus.PENDING,
        applicationReviewedAt: null
      },
      {
        ...mockUser,
        id: 'other-user',
        username: 'SecondUser',
        email: 'second@example.com',
        firstname: 'Chris',
        lastname: 'Brown',
        userApplicationStatus: UserApplicationStatus.PENDING,
        applicationReviewedAt: null
      }
    ])
    store.refreshState()
    component.formGroup.patchValue({quickFilter: []})

    component.formGroup.patchValue({search: '  captaindemo  '})
    fixture.detectChanges()
    expect(component.users().map(user => user.id)).toEqual(['matching-user'])

    component.formGroup.patchValue({search: 'CAPTAIN@EXAMPLE.COM'})
    fixture.detectChanges()
    expect(component.users().map(user => user.id)).toEqual(['matching-user'])

    component.formGroup.patchValue({search: 'aLeX'})
    fixture.detectChanges()
    expect(component.users().map(user => user.id)).toEqual(['matching-user'])

    component.formGroup.patchValue({search: 'anDerSon'})
    fixture.detectChanges()
    expect(component.users().map(user => user.id)).toEqual(['matching-user'])
  })

  it('should combine the search bar with the selected quick filters', () => {
    store.overrideSelector(selectUser, null)
    store.overrideSelector(selectUsers, [
      {
        ...mockUser,
        id: 'accepted-alex',
        username: 'accepted-alex',
        firstname: 'Alex',
        userApplicationStatus: UserApplicationStatus.ACCEPTED,
        applicationReviewedAt: new Date()
      },
      {
        ...mockUser,
        id: 'pending-alex',
        username: 'pending-alex',
        firstname: 'Alex',
        userApplicationStatus: UserApplicationStatus.PENDING,
        applicationReviewedAt: null
      },
      {
        ...mockUser,
        id: 'accepted-chris',
        username: 'accepted-chris',
        firstname: 'Chris',
        userApplicationStatus: UserApplicationStatus.ACCEPTED,
        applicationReviewedAt: new Date()
      }
    ])
    store.refreshState()
    component.formGroup.patchValue({
      quickFilter: [UserApplicationStatus.ACCEPTED],
      search: 'alex'
    })
    fixture.detectChanges()

    expect(component.users().map(user => user.id)).toEqual(['accepted-alex'])
  })

  it('should call confirmUser action on accept', () => {
    vi.spyOn(store, 'dispatch')
    const userId = 'test-user-id'
    component.accept(userId)
    expect(store.dispatch).toHaveBeenCalledWith(confirmUser({id: userId}))
  })

  it('should call denyUser action on deny', () => {
    vi.spyOn(store, 'dispatch')
    const userId = 'test-user-id'
    component.deny(userId)
    expect(store.dispatch).toHaveBeenCalledWith(denyUser({id: userId}))
  })
})
