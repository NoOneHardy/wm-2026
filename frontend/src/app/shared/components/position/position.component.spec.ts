// noinspection DuplicatedCode

import { ComponentFixture, TestBed } from '@angular/core/testing'
import { PositionComponent } from './position.component'
import {Ranking} from '../../../model/leaderboard/ranking'

describe('PositionComponent', () => {
  let component: PositionComponent
  let fixture: ComponentFixture<PositionComponent>
  let ranking: Ranking

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [PositionComponent]
    }).compileComponents()

    ranking = {
      id: 'user-1',
      ranking: 0,
      avatar: null,
      points: 100,
      prevRanking: 0,
      username: 'No1Hardy'
    }

    fixture = TestBed.createComponent(PositionComponent)
    component = fixture.componentInstance
    fixture.componentRef.setInput('ranking', ranking)
    fixture.detectChanges()
  })

  it('should create', () => {
    expect(component).toBeTruthy()
  })

  it('should parse highlight input', () => {
    expect(component.highlight()).toBe(false)
    fixture.componentRef.setInput('highlight', true)
    fixture.detectChanges()
    expect(component.highlight()).toBe(true)
    fixture.componentRef.setInput('highlight', '')
    fixture.detectChanges()
    expect(component.highlight()).toBe(true)
  })

  it('should compute movement icon', () => {
    // move up 6 positions
    ranking.prevRanking = 10
    ranking.ranking = 4
    fixture.componentRef.setInput('ranking', {...ranking})
    fixture.detectChanges()
    expect(component.movementIcon()).toBe('keyboard_double_arrow_up')

    // move up 5 positions
    ranking.prevRanking = 10
    ranking.ranking = 5
    fixture.componentRef.setInput('ranking', {...ranking})
    fixture.detectChanges()
    expect(component.movementIcon()).toBe('keyboard_double_arrow_up')

    // move up 4 positions
    ranking.prevRanking = 10
    ranking.ranking = 6
    fixture.componentRef.setInput('ranking', {...ranking})
    fixture.detectChanges()
    expect(component.movementIcon()).toBe('keyboard_arrow_up')

    // move up 1 position
    ranking.prevRanking = 10
    ranking.ranking = 9
    fixture.componentRef.setInput('ranking', {...ranking})
    fixture.detectChanges()
    expect(component.movementIcon()).toBe('keyboard_arrow_up')

    // no movement
    ranking.prevRanking = 10
    ranking.ranking = 10
    fixture.componentRef.setInput('ranking', {...ranking})
    fixture.detectChanges()
    expect(component.movementIcon()).toBe('equal')

    // move down 6 positions
    ranking.prevRanking = 10
    ranking.ranking = 16
    fixture.componentRef.setInput('ranking', {...ranking})
    fixture.detectChanges()
    expect(component.movementIcon()).toBe('keyboard_double_arrow_down')

    // move down 5 positions
    ranking.prevRanking = 10
    ranking.ranking = 15
    fixture.componentRef.setInput('ranking', {...ranking})
    fixture.detectChanges()
    expect(component.movementIcon()).toBe('keyboard_double_arrow_down')

    // move down 4 positions
    ranking.prevRanking = 10
    ranking.ranking = 14
    fixture.componentRef.setInput('ranking', {...ranking})
    fixture.detectChanges()
    expect(component.movementIcon()).toBe('keyboard_arrow_down')

    // move down 1 position
    ranking.prevRanking = 10
    ranking.ranking = 11
    fixture.componentRef.setInput('ranking', {...ranking})
    fixture.detectChanges()
    expect(component.movementIcon()).toBe('keyboard_arrow_down')
  })
})
