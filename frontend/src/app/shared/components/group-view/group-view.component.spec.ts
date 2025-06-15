import {ComponentFixture, TestBed} from '@angular/core/testing'

import {GroupViewComponent} from './group-view.component'
import {MockStore, provideMockStore} from '@ngrx/store/testing'
import {ActivatedRoute, provideRouter} from '@angular/router'
import {Group} from '../../../model/group/group'
import {selectActiveGroup, selectIsTournamentLoading, selectIsTournamentSaving} from '../../store/tournament.feature'
import {of} from 'rxjs'
import {deselectGroup, saveBets, saveResults} from '../../store/tournament.actions'

const mockGroup: Group = {
  lastSavedAtResult: new Date('2025-04-23T20:58:00'),
  lastSavedAt: new Date('2025-04-23T19:38:00'),
  percentageResult: 40,
  percentage: 50,
  name: 'Gruppe A',
  availableJokers: {
    jdouble: 0,
    jtriple: 0
  },
  games: [
    {
      id: 'game2',
      timestamp: new Date(new Date().getFullYear(), new Date().getMonth(), new Date().getDate() + 1, 18, 0, 0),
      teamHome: {
        id: 'team1',
        name: 'Team 1',
        flag: 'https://example.com/logo1.png',
        previousGames: []
      },
      teamGuest: {
        id: 'team2',
        name: 'Team 2',
        flag: 'https://example.com/logo2.png',
        previousGames: []
      },
      result: null,
      bet: null,
    },
    {
      id: 'game1',
      timestamp: new Date('2025-04-23T18:00:00'),
      teamHome: {
        id: 'team1',
        name: 'Team 1',
        flag: 'https://example.com/logo1.png',
        previousGames: []
      },
      teamGuest: {
        id: 'team2',
        name: 'Team 2',
        flag: 'https://example.com/logo2.png',
        previousGames: []
      },
      result: {
        id: 'result1',
        scoreTeamHome: 2,
        scoreTeamGuest: 1,
        gameId: 'game1',
      },
      bet: {
        id: 'bet1',
        scoreTeamHome: 2,
        scoreTeamGuest: 1,
        joker: 1,
        gameId: 'game1'
      }
    }
  ],
  id: 'group1',
  isKnockout: false
}

describe('GroupViewComponent', () => {
  let component: GroupViewComponent
  let fixture: ComponentFixture<GroupViewComponent>
  let mockStore: MockStore

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [GroupViewComponent],
      providers: [
        provideMockStore(),
        provideRouter([]),
        {
          provide: ActivatedRoute,
          useValue: {
            queryParamMap: of({
              get: () => 'game1'
            })
          }
        }
      ]
    }).compileComponents()

    fixture = TestBed.createComponent(GroupViewComponent)
    component = fixture.componentInstance
    fixture.detectChanges()
    mockStore = TestBed.inject(MockStore)
    mockStore.overrideSelector(selectActiveGroup, mockGroup)
    mockStore.refreshState()
  })

  it('should create', () => {
    expect(component).toBeTruthy()
  })

  it('should load group from store', () => {
    expect(component.group()).toEqual(mockGroup)
  })

  it('should not throw error if group is null', () => {
    mockStore.overrideSelector(selectActiveGroup, null)
    mockStore.refreshState()
    expect(() => {
      component.group()
    }).not.toThrow()
    expect(component.games).toEqual([])
  })

  it('should set highlight to query param g', () => {
    expect(component.highlight()).toEqual('game1')
  })

  it('should add form control for each game', () => {
    expect(component.form.controls.bets.length).toEqual(2)
    mockStore.overrideSelector(selectActiveGroup, null)
    mockStore.refreshState()
    fixture.detectChanges()
    expect(component.form.controls.bets.length).toEqual(0)
  })

  it('should add form control for each game with correct values', () => {
    const game1Control = component.form.controls.bets.at(0)
    const game2Control = component.form.controls.bets.at(1)

    expect(game1Control?.getRawValue()).toEqual({
      game: 'game1',
      joker: 1,
      scoreTeamHome: 2,
      scoreTeamGuest: 1
    })

    expect(game2Control?.getRawValue()).toEqual({
      game: 'game2',
      joker: 1,
      scoreTeamHome: null,
      scoreTeamGuest: null
    })
  })

  it('should save bets', () => {
    const spy = spyOn(component['store'], 'dispatch')

    component.save()

    expect(spy).toHaveBeenCalledWith(saveBets({
      groupId: 'group1',
      bets: [
        {
          game: 'game1',
          joker: 1,
          scoreTeamHome: 2,
          scoreTeamGuest: 1
        }
      ]
    }))
  })

  it('should only save bets with non-null scores', () => {
    const spy = spyOn(component['store'], 'dispatch')

    mockStore.overrideSelector(selectActiveGroup, {
      ...mockGroup,
      games: [
        {
          ...mockGroup.games[0],
          bet: {
            id: 'bet2',
            joker: 1,
            scoreTeamHome: null as unknown as number,
            scoreTeamGuest: null as unknown as number,
            gameId: 'game2',
          }
        },
        mockGroup.games[1]
      ]
    })
    mockStore.refreshState()
    fixture.detectChanges()

    component.save()

    expect(spy).toHaveBeenCalledWith(saveBets({
      groupId: 'group1',
      bets: [
        {
          game: 'game1',
          joker: 1,
          scoreTeamHome: 2,
          scoreTeamGuest: 1
        }
      ]
    }))
  })

  it('should replace null values with 0', () => {
    const spy = spyOn(component['store'], 'dispatch')

    mockStore.overrideSelector(selectActiveGroup, {
      ...mockGroup,
      games: [
        {
          ...mockGroup.games[0],
          bet: {
            id: 'bet2',
            joker: 1,
            scoreTeamHome: 2,
            scoreTeamGuest: null as unknown as number,
            gameId: 'game2',
          }
        }
      ]
    })
    mockStore.refreshState()
    fixture.detectChanges()

    component.save()

    expect(spy).toHaveBeenCalledWith(saveBets({
      groupId: 'group1',
      bets: [
        {
          game: 'game2',
          joker: 1,
          scoreTeamHome: 2,
          scoreTeamGuest: 0
        }
      ]
    }))
  })

  it('should return a game by its id', () => {
    expect(component.findGame('game1')).toEqual(mockGroup.games[1])
  })

  it('should return undefined if game is not found', () => {
    expect(component.findGame('nonexistent')).toBeUndefined()
  })

  it('should sort games by timestamp', () => {
    const sortedGames = component.games
    expect(sortedGames[0].id).toEqual('game1')
    expect(sortedGames[1].id).toEqual('game2')
  })

  it('should load loading and saving states from store', () => {
    mockStore.overrideSelector(selectIsTournamentLoading, true)
    mockStore.overrideSelector(selectIsTournamentSaving, true)
    mockStore.refreshState()
    fixture.detectChanges()

    expect(component.isLoading()).toBeTrue()
    expect(component.isSaving()).toBeTrue()

    mockStore.overrideSelector(selectIsTournamentLoading, false)
    mockStore.overrideSelector(selectIsTournamentSaving, false)
    mockStore.refreshState()
    fixture.detectChanges()

    expect(component.isLoading()).toEqual(false)
    expect(component.isSaving()).toEqual(false)
  })

  it('should deselect group', () => {
    const storeSpy = spyOn(component['store'], 'dispatch').and.callThrough()
    component.back()

    expect(storeSpy).toHaveBeenCalledWith(deselectGroup())
  })

  it('should deselect group on destroy', () => {
    const storeSpy = spyOn(component['store'], 'dispatch').and.callThrough()
    component.ngOnDestroy()

    expect(storeSpy).toHaveBeenCalledWith(deselectGroup())
  })

  it('should prepare default values for bet mode', () => {
    const game = mockGroup.games[0]

    const form = component['getDefaultValues'](game)
    expect(form).toEqual({
      game: game.id,
      joker: 1,
      scoreTeamHome: null,
      scoreTeamGuest: null
    })
  })

  it('should prepare default values for bet mode with existing bet', () => {
    const game = {...mockGroup.games[1]}
    game.bet = {
      id: 'bet-2',
      gameId: game.id,
      joker: 3,
      scoreTeamHome: 2,
      scoreTeamGuest: 1
    }

    const form = component['getDefaultValues'](game)
    expect(form).toEqual({
      game: game.id,
      joker: 3,
      scoreTeamHome: 2,
      scoreTeamGuest: 1
    })
  })

  it('should prepare default values for admin mode', () => {
    fixture.componentRef.setInput('mode', 'admin')
    fixture.detectChanges()

    const game = mockGroup.games[0]
    const form = component['getDefaultValues'](game)
    expect(form).toEqual({
      game: game.id,
      joker: 1,
      scoreTeamHome: null,
      scoreTeamGuest: null
    })
  })

  it('should prepare default values for admin mode with existing result', () => {
    fixture.componentRef.setInput('mode', 'admin')
    fixture.detectChanges()

    const game = {...mockGroup.games[1]}
    game.result = {
      id: 'result-2',
      gameId: game.id,
      scoreTeamHome: 3,
      scoreTeamGuest: 3
    }

    const form = component['getDefaultValues'](game)
    expect(form).toEqual({
      game: game.id,
      joker: 1,
      scoreTeamHome: 3,
      scoreTeamGuest: 3
    })
  })

  it('should save bets in bet mode', () => {
    const spy = spyOn(component['store'], 'dispatch')
    component.save()

    expect(spy).toHaveBeenCalledWith(saveBets({
      groupId: 'group1',
      bets: [
        {
          game: 'game1',
          scoreTeamHome: 2,
          scoreTeamGuest: 1,
          joker: 1
        }
      ]
    }))
  })

  it('should save results in admin mode', () => {
    fixture.componentRef.setInput('mode', 'admin')
    const spy = spyOn(component['store'], 'dispatch')

    component.form.controls.bets.patchValue([
      {
        game: 'game1',
        scoreTeamHome: 2,
        scoreTeamGuest: 1,
        joker: 1
      },
      {
        game: 'game2',
        scoreTeamHome: 3,
        scoreTeamGuest: 3,
        joker: 2
      }
    ])

    component.save()

    expect(spy).toHaveBeenCalledWith(saveResults({
      groupId: 'group1',
      results: [
        {
          game: 'game1',
          scoreTeamHome: 2,
          scoreTeamGuest: 1
        },
        {
          game: 'game2',
          scoreTeamHome: 3,
          scoreTeamGuest: 3
        }
      ]
    }))
  })
})
