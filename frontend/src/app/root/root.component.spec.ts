import {ComponentFixture, TestBed} from '@angular/core/testing'

import {RootComponent} from './root.component'
import {MockStore, provideMockStore} from '@ngrx/store/testing'
import {User} from '../model/user/user'
import {mockUser1} from '../model/mock/user.mock'
import {selectUser} from '../user-management/store/user.feature'
import {provideRouter} from '@angular/router'
import {selectHomeData} from '../home/store/home.feature'

describe('RootComponent', () => {
  let component: RootComponent
  let fixture: ComponentFixture<RootComponent>
  let store: MockStore
  let mockUser: User

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [RootComponent],
      providers: [provideMockStore(), provideRouter([])]
    }).compileComponents()

    fixture = TestBed.createComponent(RootComponent)
    mockUser = {...mockUser1}
    store = TestBed.inject(MockStore)
    store.overrideSelector(selectHomeData, null)
    component = fixture.componentInstance
    store.refreshState()
    fixture.detectChanges()
  })

  it('should create', () => {
    expect(component).toBeTruthy()
  })

  it('should have user as null initially', () => {
    store.overrideSelector(selectUser, null)
    store.refreshState()
    fixture.detectChanges()
    expect(component.user()).toBeNull()
  })

  it('should have isLoggedIn as false initially', () => {
    store.overrideSelector(selectUser, null)
    store.refreshState()
    fixture.detectChanges()
    expect(component.isLoggedIn()).toBe(false)
  })

  it('should update user when store emits a new user', () => {
    store.overrideSelector(selectUser, mockUser)
    store.refreshState()
    fixture.detectChanges()
    expect(component.user()).toEqual(mockUser)
  })

  it('should update isLoggedIn when user changes', () => {
    store.overrideSelector(selectUser, mockUser)
    store.refreshState()
    fixture.detectChanges()
    expect(component.isLoggedIn()).toBe(true)
  })
})
