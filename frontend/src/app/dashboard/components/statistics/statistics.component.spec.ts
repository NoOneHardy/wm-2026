import { ComponentFixture, TestBed } from '@angular/core/testing'

import { StatisticsComponent } from './statistics.component'

describe('StatisticsComponent', () => {
  let component: StatisticsComponent
  let fixture: ComponentFixture<StatisticsComponent>

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [StatisticsComponent]
    }).compileComponents()

    fixture = TestBed.createComponent(StatisticsComponent)
    component = fixture.componentInstance
    fixture.componentRef.setInput('personalStats', {})
    fixture.componentRef.setInput('globalStats', {})
    fixture.detectChanges()
  })

  it('should create', () => {
    expect(component).toBeTruthy()
  })
})
