import {ComponentFixture, TestBed} from '@angular/core/testing'

import {StatisticsComponent} from './statistics.component'
import {provideMockStore} from '@ngrx/store/testing'

describe('StatisticsComponent', () => {
  let component: StatisticsComponent
  let fixture: ComponentFixture<StatisticsComponent>

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [StatisticsComponent],
      providers: [provideMockStore()]
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
