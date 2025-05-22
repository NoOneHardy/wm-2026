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
      ranking: 1,
      avatar: null,
      points: 100,
      previousRanking: 3,
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
})
