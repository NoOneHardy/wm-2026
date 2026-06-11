import {ComponentFixture, TestBed} from '@angular/core/testing'

import {GameListItemComponent} from './game-list-item.component'
import {provideRouter, Router} from '@angular/router'
import {mockBetGame1, mockBetGame2, mockBetGame3} from '../../../model/mock/bet-game.mock'

describe('GameListItemComponent', () => {
  let component: GameListItemComponent
  let fixture: ComponentFixture<GameListItemComponent>

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [GameListItemComponent],
      providers: [provideRouter([])]
    }).compileComponents()

    fixture = TestBed.createComponent(GameListItemComponent)
    component = fixture.componentInstance
    fixture.componentRef.setInput('game', mockBetGame1)
    fixture.detectChanges()
  })

  it('should create', () => {
    expect(component).toBeTruthy()
  })

  it('should show an open bet warning for an upcoming game without bet', () => {
    const element: HTMLElement = fixture.nativeElement
    expect(element.querySelector('.chip.-warn')).toBeTruthy()
  })

  it('should show the bet for an upcoming game with bet', () => {
    fixture.componentRef.setInput('game', mockBetGame2)
    fixture.detectChanges()

    const element: HTMLElement = fixture.nativeElement
    expect(element.querySelector('.chip.-bet')?.textContent).toContain('Tipp 2:1')
  })

  it('should show the earned points for a played game', () => {
    fixture.componentRef.setInput('game', mockBetGame3)
    fixture.detectChanges()

    const element: HTMLElement = fixture.nativeElement
    expect(element.querySelector('.chip.-result .points')?.textContent).toContain('+20')
  })

  it('should navigate to the game details on click', () => {
    const router = TestBed.inject(Router)
    const navigateSpy = spyOn(router, 'navigateByUrl').and.callThrough()

    const button: HTMLButtonElement = fixture.nativeElement.querySelector('button.row')
    button.click()

    expect(navigateSpy).toHaveBeenCalledWith('/bets/group-1/game-1')
  })
})
