import {ComponentFixture, TestBed} from '@angular/core/testing'

import {RemainingJokerDisplayComponent} from './remaining-joker-display.component'

describe('RemainingJokerDisplayComponent', () => {
  let component: RemainingJokerDisplayComponent
  let fixture: ComponentFixture<RemainingJokerDisplayComponent>

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [RemainingJokerDisplayComponent]
    })
      .compileComponents()

    fixture = TestBed.createComponent(RemainingJokerDisplayComponent)
    component = fixture.componentInstance
    fixture.componentRef.setInput('jokerMultiplier', 2)
    fixture.componentRef.setInput('jokersRemaining', 5)
    fixture.componentRef.setInput('jokersMax', 10)
    fixture.detectChanges()
  })

  it('should create', () => {
    expect(component).toBeTruthy()
  })

  it('should generate joker array correctly', () => {
    expect(component.jokerArray.length).toBe(2)
    expect(component.jokerArray).toEqual([0, 1])

    fixture.componentRef.setInput('jokerMultiplier', 3)
    fixture.detectChanges()

    expect(component.jokerArray.length).toBe(3)
    expect(component.jokerArray).toEqual([0, 1, 2])
  })
})
