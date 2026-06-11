import {ComponentFixture, TestBed} from '@angular/core/testing'

import {DashboardComponent} from './dashboard.component'
import {MockStore, provideMockStore} from '@ngrx/store/testing'
import {provideRouter} from '@angular/router'
import {selectDashboard} from '../shared/store/tournament.feature'
import {DashboardData} from '../model/dashboard/dashboard-data'
import {mockBetGame1, mockBetGame2, mockBetGame3} from '../model/mock/bet-game.mock'
import {Ranking} from '../model/leaderboard/ranking'

const mockRanking: Ranking = Object.freeze({
  id: 'user-1',
  username: 'User1',
  points: 100,
  avatar: null,
  ranking: 1,
  prevRanking: 2
})

const mockDashboardData: DashboardData = Object.freeze({
  leaderboardPreview: [mockRanking, null],
  userSummary: {
    points: 100,
    ranking: 1,
    percentage: 75,
    isConfirmed: true
  },
  stats: {
    totalGoalsBet: 42,
    correctGames: 7,
    jokersWasted: 1
  },
  globalStats: {
    totalPoints: 1337,
    correctGames: 100,
    jokersWasted: 13
  },
  upcomingGames: [mockBetGame2, mockBetGame1],
  recentResults: [mockBetGame3]
})

describe('DashboardComponent', () => {
  let component: DashboardComponent
  let fixture: ComponentFixture<DashboardComponent>
  let store: MockStore

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [DashboardComponent],
      providers: [provideMockStore(), provideRouter([])]
    }).compileComponents()

    fixture = TestBed.createComponent(DashboardComponent)
    store = TestBed.inject(MockStore)
    component = fixture.componentInstance
    fixture.detectChanges()
  })

  it('should create', () => {
    expect(component).toBeTruthy()
  })

  describe('with dashboard data', () => {
    beforeEach(() => {
      store.overrideSelector(selectDashboard, {...mockDashboardData})
      store.refreshState()
      fixture.detectChanges()
    })

    it('should sort upcoming games ascending by timestamp', () => {
      expect(component.upcomingGames().map(game => game.id)).toEqual(['game-1', 'game-2'])
    })

    it('should use the earliest upcoming game as next game', () => {
      expect(component.nextGame()?.id).toBe('game-1')
    })

    it('should exclude the next game from the later games', () => {
      expect(component.laterGames().map(game => game.id)).toEqual(['game-2'])
    })

    it('should count upcoming games without a bet as open bets', () => {
      expect(component.openBetCount()).toBe(1)
    })

    it('should filter empty entries from the leaderboard preview', () => {
      expect(component.leaderboardPreview()).toEqual([mockRanking])
    })
  })

  it('should have no next game without data', () => {
    expect(component.nextGame()).toBeNull()
    expect(component.openBetCount()).toBe(0)
  })
})
