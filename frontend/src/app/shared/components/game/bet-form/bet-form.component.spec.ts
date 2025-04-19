import {ComponentFixture, TestBed} from '@angular/core/testing'

import {BetFormComponent} from './bet-form.component'
import {BetGame} from '../../../../model/game/bet-game'

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
  let component: BetFormComponent
  let fixture: ComponentFixture<BetFormComponent>

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [BetFormComponent]
    }).compileComponents()

    fixture = TestBed.createComponent(BetFormComponent)
    fixture.componentRef.setInput('game', mockGame)
    component = fixture.componentInstance
    fixture.detectChanges()
  })

  it('should create', () => {
    expect(component).toBeTruthy()
  })
})
