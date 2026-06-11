import {ComponentFixture, TestBed} from '@angular/core/testing'

import {StatsPanelComponent} from './stats-panel.component'

describe('StatsPanelComponent', () => {
  let component: StatsPanelComponent
  let fixture: ComponentFixture<StatsPanelComponent>

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [StatsPanelComponent]
    }).compileComponents()

    fixture = TestBed.createComponent(StatsPanelComponent)
    component = fixture.componentInstance
    fixture.componentRef.setInput('personalStats', {totalGoalsBet: 42, correctGames: 7, jokersWasted: 1})
    fixture.componentRef.setInput('globalStats', {totalPoints: 1337, correctGames: 100, jokersWasted: 13})
    fixture.detectChanges()
  })

  it('should create', () => {
    expect(component).toBeTruthy()
  })

  it('should show personal stats by default', () => {
    expect(component.showGlobalStats()).toBeFalse()
    expect(component.stats().map(s => s.value)).toEqual([7, 42, 1])
  })

  it('should show global stats when toggled', () => {
    component.showGlobalStats.set(true)
    fixture.detectChanges()

    expect(component.stats().map(s => s.value)).toEqual([100, 1337, 13])
  })

  it('should return no stats when data is missing', () => {
    fixture.componentRef.setInput('globalStats', undefined)
    fixture.detectChanges()

    expect(component.stats()).toEqual([])
  })
})
