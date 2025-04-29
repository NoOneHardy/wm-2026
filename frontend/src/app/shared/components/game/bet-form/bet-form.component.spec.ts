import {ComponentFixture, TestBed} from '@angular/core/testing'

import {BetFormComponent} from './bet-form.component'
import {BetGame} from '../../../../model/game/bet-game'
import {MockStore, provideMockStore} from '@ngrx/store/testing'
import {selectAvailableJokers} from '../../../store/tournament.feature'
import {grantJDouble, grantJTriple, revokeJDouble, revokeJTriple} from '../../../store/tournament.actions'

const mockGame: BetGame = {
  id: 'game-1',
  timestamp: new Date('2025-06-23T21:00:00'),
  teamHome: {
    id: 'team-1',
    name: 'Deutschland',
    flag: 'https://upload.wikimedia.org/wikipedia/commons/thumb/b/ba/Flag_of_Germany.svg/180px-Flag_of_Germany.svg.png',
    createdAt: new Date('2025-04-13T17:37:13.470342'),
    updatedAt: new Date('2025-04-13T17:37:13.470342'),
    deletedAt: null
  },
  teamGuest: {
    id: 'team-2',
    name: 'Schottland',
    flag: 'https://upload.wikimedia.org/wikipedia/commons/thumb/1/10/Flag_of_Scotland.svg/250px-Flag_of_Scotland.svg.png',
    createdAt: new Date('2025-04-13T17:37:44.985522'),
    updatedAt: new Date('2025-04-13T17:37:44.985522'),
    deletedAt: null
  },
  result: null,
  bet: {
    id: 'asdf-1',
    scoreTeamHome: 3,
    scoreTeamGuest: 2,
    joker: 3,
    gameId: 'bet-1',
    createdAt: new Date(),
    updatedAt: new Date(),
    deletedAt: null
  },
  createdAt: new Date('2025-04-13T19:37:58.15714'),
  updatedAt: new Date('2025-04-19T21:23:00.972354'),
  deletedAt: null
}

describe('BetFormComponent', () => {
  let mockStore: MockStore
  let component: BetFormComponent
  let fixture: ComponentFixture<BetFormComponent>

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [BetFormComponent],
      providers: [provideMockStore()]
    }).compileComponents()

    mockStore = TestBed.inject(MockStore)
    mockStore.overrideSelector(selectAvailableJokers, {
      jdouble: 5,
      jtriple: 3
    })
    mockStore.refreshState()
    fixture = TestBed.createComponent(BetFormComponent)
    fixture.componentRef.setInput('game', mockGame)
    component = fixture.componentInstance
    fixture.detectChanges()
  })

  it('should create', () => {
    expect(component).toBeTruthy()
  })

  it('should load available jokers from store', () => {
    expect(component.availableJokers()).toEqual({
      jdouble: 5,
      jtriple: 3
    })
  })

  it('should map highlight to boolean', () => {
    expect(component.highlight()).toBe(false)
    fixture.componentRef.setInput('highlight', true)
    fixture.detectChanges()
    expect(component.highlight()).toBe(true)
    fixture.componentRef.setInput('highlight', '')
    fixture.detectChanges()
    expect(component.highlight()).toBe(true)
  })

  it('should evaluate whether a game has started', () => {
    jasmine.clock().install()
    jasmine.clock().mockDate(new Date('2025-06-23T20:00:00'))
    fixture.componentRef.setInput('game', {
      ...mockGame,
      timestamp: new Date('2025-06-23T22:00:00')
    })
    fixture.detectChanges()
    expect(component.hasStarted()).toBe(false)

    jasmine.clock().mockDate(new Date('2025-06-23T22:00:00'))
    fixture.componentRef.setInput('game', {
      ...mockGame,
      timestamp: new Date('2025-06-23T22:00:00')
    })
    fixture.detectChanges()
    expect(component.hasStarted()).toBe(true)

    jasmine.clock().uninstall()
  })

  it('should disable form when game has started', () => {
    jasmine.clock().install()
    jasmine.clock().mockDate(new Date('2025-06-23T20:00:00'))
    fixture.componentRef.setInput('game', {
      ...mockGame,
      timestamp: new Date('2025-06-23T22:00:00')
    })
    fixture.detectChanges()
    expect(component.isDisabled).toBe(false)

    jasmine.clock().mockDate(new Date('2025-06-23T22:00:00'))
    fixture.componentRef.setInput('game', {
      ...mockGame,
      timestamp: new Date('2025-06-23T22:00:00')
    })
    fixture.detectChanges()
    expect(component.isDisabled).toBe(true)

    jasmine.clock().uninstall()
  })

  it('should disable form when game has result', () => {
    fixture.componentRef.setInput('game', {
      ...mockGame,
      result: {
        scoreTeamHome: 2,
        scoreTeamGuest: 3
      }
    })
    fixture.detectChanges()
    expect(component.isDisabled).toBe(true)
  })

  it('should set form value when game changes', () => {
    expect(component.formGroup.getRawValue()).toEqual({
      scoreTeamHome: 3,
      scoreTeamGuest: 2,
      joker: 3
    })
    fixture.componentRef.setInput('game', {
      ...mockGame,
      bet: {
        ...mockGame.bet,
        scoreTeamHome: 1,
        scoreTeamGuest: 1
      }
    })
    fixture.detectChanges()
    expect(component.formGroup.getRawValue()).toEqual({
      joker: 3,
      scoreTeamHome: 1,
      scoreTeamGuest: 1
    })
  })

  it('should set control value when form value is changing', () => {
    expect(component['value']).toEqual({
      scoreTeamHome: 3,
      scoreTeamGuest: 2,
      joker: 3,
      game: 'game-1'
    })

    component.formGroup.setValue({
      scoreTeamHome: 1,
      scoreTeamGuest: 1,
      joker: 2
    })
    fixture.detectChanges()
    expect(component['value']).toEqual({
      scoreTeamHome: 1,
      scoreTeamGuest: 1,
      joker: 2,
      game: 'game-1'
    })
  })

  it('should set form value when control value is set', () => {
    expect(component.formGroup.getRawValue()).toEqual({
      scoreTeamHome: 3,
      scoreTeamGuest: 2,
      joker: 3
    })

    component.writeValue({
      scoreTeamHome: 1,
      scoreTeamGuest: 1,
      joker: 2
    })
    fixture.detectChanges()
    expect(component.formGroup.getRawValue()).toEqual({
      joker: 2,
      scoreTeamHome: 1,
      scoreTeamGuest: 1
    })
  })

  it('should disable form group when control is disabled', () => {
    expect(component.formGroup.disabled).toBe(false)
    component.setDisabledState(true)
    fixture.detectChanges()
    expect(component.formGroup.disabled).toBe(true)
    component.setDisabledState(false)
    fixture.detectChanges()
    expect(component.formGroup.disabled).toBe(false)
  })

  it('should check whether a joker has to be displayed selected', () => {
    component.formGroup.controls.joker.setValue(1)
    expect(component.fillJoker(1)).toBe(true)
    expect(component.fillJoker(2)).toBe(false)
    expect(component.fillJoker(3)).toBe(false)

    component.formGroup.controls.joker.setValue(2)
    expect(component.fillJoker(1)).toBe(true)
    expect(component.fillJoker(2)).toBe(true)
    expect(component.fillJoker(3)).toBe(false)

    component.formGroup.controls.joker.setValue(3)
    expect(component.fillJoker(1)).toBe(true)
    expect(component.fillJoker(2)).toBe(true)
    expect(component.fillJoker(3)).toBe(true)
  })

  it('should return false if joker is higher than 3', () => {
    expect(component.fillJoker(4)).toBe(false)
  })

  it('should return true if checked joker is 1', () => {
    expect(component.hasJokersAvailable(1)).toBeTrue()
  })

  it('should return true if checked joker is 2 and available jokers are greater than 0', () => {
    expect(component.hasJokersAvailable(2)).toBeTrue()
  })

  it('should return false if checked joker is 2 and available jokers are 0', () => {
    mockStore.overrideSelector(selectAvailableJokers, {
      jdouble: 0,
      jtriple: 3
    })
    mockStore.refreshState()
    expect(component.hasJokersAvailable(2)).toBeFalse()
  })

  it('should return true if checked joker is 3 and available jokers are greater than 0', () => {
    expect(component.hasJokersAvailable(3)).toBeTrue()
  })

  it('should return false if checked joker is 3 and available jokers are 0', () => {
    mockStore.overrideSelector(selectAvailableJokers, {
      jdouble: 5,
      jtriple: 0
    })
    mockStore.refreshState()
    expect(component.hasJokersAvailable(3)).toBeFalse()
  })

  it('should return false if checked joker is not 1, 2 or 3', () => {
    expect(component.hasJokersAvailable(4)).toBeFalse()
  })

  it('should grant a joker if joker has changed and was limited', () => {
    const spy = spyOn(mockStore, 'dispatch')

    expect(component['value'].joker).toBe(3)
    component.formGroup.controls.joker.setValue(2)
    fixture.detectChanges()
    expect(component['value'].joker).toBe(2)
    expect(spy).toHaveBeenCalledWith(grantJTriple())

    component.formGroup.controls.joker.setValue(1)
    fixture.detectChanges()
    expect(component['value'].joker).toBe(1)
    expect(spy).toHaveBeenCalledWith(grantJDouble())
  })

  it('should revoke a joker if joker has changed and was limited', () => {
    const spy = spyOn(mockStore, 'dispatch')

    component.formGroup.controls.joker.setValue(2)
    fixture.detectChanges()
    expect(component['value'].joker).toBe(2)
    expect(spy).toHaveBeenCalledWith(revokeJDouble())

    component.formGroup.controls.joker.setValue(3)
    fixture.detectChanges()
    expect(component['value'].joker).toBe(3)
    expect(spy).toHaveBeenCalledWith(revokeJTriple())
  })

  it('should not revoke or grant a joker if joker has not changed', () => {
    const spy = spyOn(mockStore, 'dispatch')

    component.formGroup.setValue({
      scoreTeamHome: 1,
      scoreTeamGuest: 2,
      joker: 3
    })
    fixture.detectChanges()
    expect(component['value'].joker).toBe(3)
    expect(spy).not.toHaveBeenCalled()
  })

  it('should not revoke or grant a joker if joker is 1', () => {
    const spy = spyOn(mockStore, 'dispatch')

    component.formGroup.controls.joker.setValue(1)
    fixture.detectChanges()
    expect(component['value'].joker).toBe(1)
    expect(spy).toHaveBeenCalledWith(grantJTriple())
    expect(spy).toHaveBeenCalledTimes(1)

    component.formGroup.controls.joker.setValue(2)
    fixture.detectChanges()
    expect(component['value'].joker).toBe(2)
    expect(spy).toHaveBeenCalledWith(revokeJDouble())
    expect(spy).toHaveBeenCalledTimes(2)
  })
})
