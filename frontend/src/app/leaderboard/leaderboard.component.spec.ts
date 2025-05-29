import {ComponentFixture, TestBed} from '@angular/core/testing'

import {LeaderboardComponent} from './leaderboard.component'
import {MockStore, provideMockStore} from '@ngrx/store/testing'
import {selectLeaderboard} from '../shared/store/tournament.feature'
import {getLeaderboard} from '../shared/store/tournament.actions'
import {selectUser} from '../user-management/store/user.feature'
import {User} from '../model/user/user'
import {Role} from '../model/user/role'
import {Ranking} from '../model/leaderboard/ranking'

describe('LeaderboardComponent', () => {
  let component: LeaderboardComponent
  let fixture: ComponentFixture<LeaderboardComponent>
  let store: MockStore

  let mockUser: User

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [LeaderboardComponent],
      providers: [provideMockStore()]
    }).compileComponents()

    mockUser = {
      id: 'user-1',
      username: 'No1Hardy',
      email: 'no1hardy@no1hardy.ch',
      role: Role.ADMIN,
      firstname: 'No1Hardy',
      lastname: 'No1Hardy',
      createdAt: new Date(),
      points: 1000,
      updatedAt: new Date(),
      isActive: true
    }

    store = TestBed.inject(MockStore)
    store.overrideSelector(selectLeaderboard, [])
    store.refreshState()
    fixture = TestBed.createComponent(LeaderboardComponent)
    component = fixture.componentInstance
    fixture.detectChanges()
  })

  it('should create', () => {
    expect(component).toBeTruthy()
  })

  it('should load leaderboard from store', () => {
    const spy = spyOn(store, 'dispatch')
    component.ngOnInit()
    expect(spy).toHaveBeenCalledWith(getLeaderboard())
    expect(component.leaderboard()).toEqual([])
  })

  it('should load user from store', () => {
    store.overrideSelector(selectUser, mockUser)
    store.refreshState()
    expect(component.user()).toEqual(mockUser)
  })

  it('should find user position in leaderboard', () => {
    const leaderboard: Ranking[] = [
      {id: 'user-1', username: 'No1Hardy', points: 1000, ranking: 1, avatar: null, previousRanking: 2},
      {id: 'user-2', username: 'UserTwo', points: 900, ranking: 2, avatar: null, previousRanking: 2}
    ]
    store.overrideSelector(selectLeaderboard, leaderboard)
    store.overrideSelector(selectUser, mockUser)
    store.refreshState()

    component.ngOnInit()

    expect(component.userPos()).toEqual(leaderboard[0])
  })

  it('should return null if user is not in leaderboard', () => {
    const leaderboard: Ranking[] = [
      {id: 'user-3', username: 'No1Hardy', points: 1000, ranking: 1, avatar: null, previousRanking: 2},
      {id: 'user-2', username: 'UserTwo', points: 900, ranking: 2, avatar: null, previousRanking: 2}
    ]
    store.overrideSelector(selectLeaderboard, leaderboard)
    store.overrideSelector(selectUser, mockUser)
    store.refreshState()

    component.ngOnInit()

    expect(component.userPos()).toBeNull()
  })

  it('should return null if user is not logged in', () => {
    const leaderboard: Ranking[] = [
      {id: 'user-1', username: 'No1Hardy', points: 1000, ranking: 1, avatar: null, previousRanking: 2},
      {id: 'user-2', username: 'UserTwo', points: 900, ranking: 2, avatar: null, previousRanking: 2}
    ]
    store.overrideSelector(selectLeaderboard, leaderboard)
    store.overrideSelector(selectUser, null)
    store.refreshState()

    component.ngOnInit()

    expect(component.userPos()).toBeNull()
  })
})
