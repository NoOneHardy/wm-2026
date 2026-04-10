import {ComponentFixture, TestBed} from '@angular/core/testing'

import {LeaderboardComponent} from './leaderboard.component'
import {MockStore, provideMockStore} from '@ngrx/store/testing'
import {selectLeaderboard} from '../shared/store/tournament.feature'
import {getLeaderboard} from '../shared/store/tournament.actions'
import {selectUser} from '../user-management/store/user.feature'
import {User} from '../model/user/user'
import {Ranking} from '../model/leaderboard/ranking'
import {mockUser1} from '../model/mock/user.mock'

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

    mockUser = {...mockUser1}

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
      {id: 'user-1', username: 'No1Hardy', points: 1000, ranking: 1, avatar: null, prevRanking: 2},
      {id: 'user-2', username: 'UserTwo', points: 900, ranking: 2, avatar: null, prevRanking: 2}
    ]
    store.overrideSelector(selectLeaderboard, leaderboard)
    store.overrideSelector(selectUser, mockUser)
    store.refreshState()

    component.ngOnInit()

    expect(component.userPos()).toEqual(leaderboard[0])
  })

  it('should return null if user is not in leaderboard', () => {
    const leaderboard: Ranking[] = [
      {id: 'user-3', username: 'No1Hardy', points: 1000, ranking: 1, avatar: null, prevRanking: 2},
      {id: 'user-2', username: 'UserTwo', points: 900, ranking: 2, avatar: null, prevRanking: 2}
    ]
    store.overrideSelector(selectLeaderboard, leaderboard)
    store.overrideSelector(selectUser, mockUser)
    store.refreshState()

    component.ngOnInit()

    expect(component.userPos()).toBeNull()
  })

  it('should return null if user is not logged in', () => {
    const leaderboard: Ranking[] = [
      {id: 'user-1', username: 'No1Hardy', points: 1000, ranking: 1, avatar: null, prevRanking: 2},
      {id: 'user-2', username: 'UserTwo', points: 900, ranking: 2, avatar: null, prevRanking: 2}
    ]
    store.overrideSelector(selectLeaderboard, leaderboard)
    store.overrideSelector(selectUser, null)
    store.refreshState()

    component.ngOnInit()

    expect(component.userPos()).toBeNull()
  })

  it('should return subHeading +1 based on user position', () => {
    const leaderboard: Ranking[] = [
      {id: mockUser.id, username: mockUser.username, points: 1000, ranking: 1, avatar: null, prevRanking: 2}
    ]
    store.overrideSelector(selectLeaderboard, leaderboard)
    store.overrideSelector(selectUser, mockUser)
    store.refreshState()

    component.ngOnInit()

    expect(component.subHeading).toBe('Gratuliere, du bist 1 Rang aufgestiegen!')
  })

  it('should return subHeading +2 based on user position', () => {
    const leaderboard: Ranking[] = [
      {id: mockUser.id, username: mockUser.username, points: 1000, ranking: 1, avatar: null, prevRanking: 3}
    ]
    store.overrideSelector(selectLeaderboard, leaderboard)
    store.overrideSelector(selectUser, mockUser)
    store.refreshState()

    component.ngOnInit()

    expect(component.subHeading).toBe('Gratuliere, du bist 2 Ränge aufgestiegen!')
  })

  it('should return subHeading -1 based on user position', () => {
    const leaderboard: Ranking[] = [
      {id: mockUser.id, username: mockUser.username, points: 1000, ranking: 2, avatar: null, prevRanking: 1}
    ]
    store.overrideSelector(selectLeaderboard, leaderboard)
    store.overrideSelector(selectUser, mockUser)
    store.refreshState()

    component.ngOnInit()

    expect(component.subHeading).toBe('Schade, du bist 1 Rang abgestiegen.')
  })

  it('should return subHeading -2 based on user position', () => {
    const leaderboard: Ranking[] = [
      {id: mockUser.id, username: mockUser.username, points: 1000, ranking: 3, avatar: null, prevRanking: 1}
    ]
    store.overrideSelector(selectLeaderboard, leaderboard)
    store.overrideSelector(selectUser, mockUser)
    store.refreshState()

    component.ngOnInit()

    expect(component.subHeading).toBe('Schade, du bist 2 Ränge abgestiegen.')
  })

  it('should return no-movement subHeading if user did not move', () => {
    const leaderboard: Ranking[] = [
      {id: 'user-1', username: 'No1Hardy', points: 1000, ranking: 1, avatar: null, prevRanking: 1},
    ]
    store.overrideSelector(selectLeaderboard, leaderboard)
    store.overrideSelector(selectUser, mockUser)
    store.refreshState()

    component.ngOnInit()

    expect(component.subHeading).toBe('Keine Veränderung, das ist auch eine Leistung!')
  })

  it('should return default subHeading if user is not logged in', () => {
    const leaderboard: Ranking[] = [
      {id: 'user-1', username: 'No1Hardy', points: 1000, ranking: 1, avatar: null, prevRanking: 1},
    ]
    store.overrideSelector(selectLeaderboard, leaderboard)
    store.overrideSelector(selectUser, null)
    store.refreshState()

    component.ngOnInit()

    expect(component.subHeading).toBe('Welche Position erreichst du?')
  })
})
