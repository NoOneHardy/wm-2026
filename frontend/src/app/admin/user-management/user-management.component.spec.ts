import {ComponentFixture, TestBed} from '@angular/core/testing'
import {MockStore, provideMockStore} from '@ngrx/store/testing'
import {UserManagementComponent} from './user-management.component'
import {selectUsers} from '../store/admin.feature'

describe('UserManagementComponent', () => {
  let component: UserManagementComponent
  let fixture: ComponentFixture<UserManagementComponent>
  let store: MockStore

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [UserManagementComponent],
      providers: [provideMockStore()]
    }).compileComponents()

    fixture = TestBed.createComponent(UserManagementComponent)
    component = fixture.componentInstance
    store = TestBed.inject(MockStore)

    store.overrideSelector(selectUsers, [])
    fixture.detectChanges()
  })

  it('should create', () => {
    expect(component).toBeTruthy()
  })
})
