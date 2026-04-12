import { ComponentFixture, TestBed } from '@angular/core/testing'

import { GamePreviewComponent } from './game-preview.component'
import {BetGame} from '../../../../model/game/bet-game'

describe('GamePreviewComponent', () => {
  let component: GamePreviewComponent
  let fixture: ComponentFixture<GamePreviewComponent>
  let mockGame: BetGame

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [GamePreviewComponent]
    }).compileComponents()

    mockGame = {
      groupId: 'group-1',
      groupName: 'Gruppe A',
      id: 'game-1',
      timestamp: new Date(
        new Date().getFullYear(),
        new Date().getMonth(),
        new Date().getDate() + 1
      ),
      teamHome: {
        id: 'team-1',
        name: 'Deutschland',
        flag: 'https://upload.wikimedia.org/wikipedia/commons/thumb/b/ba/Flag_of_Germany.svg/180px-Flag_of_Germany.svg.png',
        previousGames: [],
        shortName: ''
      },
      teamGuest: {
        id: 'team-2',
        name: 'Schottland',
        flag: 'https://upload.wikimedia.org/wikipedia/commons/thumb/1/10/Flag_of_Scotland.svg/250px-Flag_of_Scotland.svg.png',
        previousGames: [],
        shortName: ''
      },
      result: null,
      bet: {
        id: 'asdf-1',
        scoreTeamHome: 3,
        scoreTeamGuest: 2,
        joker: 3,
        gameId: 'bet-1'
      }
    }

    fixture = TestBed.createComponent(GamePreviewComponent)
    component = fixture.componentInstance
    fixture.componentRef.setInput('game', {...mockGame})
    fixture.detectChanges()
  })

  it('should create', () => {
    expect(component).toBeTruthy()
  })
})
