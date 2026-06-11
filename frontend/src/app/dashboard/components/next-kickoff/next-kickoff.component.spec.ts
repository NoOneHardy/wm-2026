import {ComponentFixture, TestBed} from '@angular/core/testing'

import {NextKickoffComponent} from './next-kickoff.component'
import {provideRouter} from '@angular/router'
import {mockBetGame1, mockBetGame2} from '../../../model/mock/bet-game.mock'
import {BetGame} from '../../../model/game/bet-game'

describe('NextKickoffComponent', () => {
  let component: NextKickoffComponent
  let fixture: ComponentFixture<NextKickoffComponent>

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [NextKickoffComponent],
      providers: [provideRouter([])]
    }).compileComponents()

    fixture = TestBed.createComponent(NextKickoffComponent)
    component = fixture.componentInstance
    fixture.detectChanges()
  })

  it('should create', () => {
    expect(component).toBeTruthy()
  })

  it('should show an empty state without a game', () => {
    const element: HTMLElement = fixture.nativeElement
    expect(element.querySelector('.empty')).toBeTruthy()
  })

  it('should calculate the countdown for a future game', () => {
    const game: BetGame = {
      ...mockBetGame1,
      timestamp: new Date(Date.now() + 90061000) // 1d 1h 1m 1s
    }
    fixture.componentRef.setInput('game', game)
    fixture.detectChanges()

    const countdown = component.countdown()
    expect(countdown).not.toBeNull()
    expect(countdown?.days).toBe(1)
    expect(countdown?.hours).toBe(1)
    expect(countdown?.minutes).toBe(1)
  })

  it('should return null as countdown for a game in the past', () => {
    const game: BetGame = {
      ...mockBetGame1,
      timestamp: new Date(Date.now() - 1000)
    }
    fixture.componentRef.setInput('game', game)
    fixture.detectChanges()

    expect(component.countdown()).toBeNull()
  })

  it('should not show joker stars for a single joker bet', () => {
    fixture.componentRef.setInput('game', mockBetGame2)
    fixture.detectChanges()

    expect(component.jokerStars()).toEqual([])
  })

  it('should show joker stars for a multiplied bet', () => {
    const game: BetGame = {
      ...mockBetGame2,
      bet: {...mockBetGame2.bet!, joker: 3}
    }
    fixture.componentRef.setInput('game', game)
    fixture.detectChanges()

    expect(component.jokerStars()).toEqual([1, 2, 3])
  })
})
