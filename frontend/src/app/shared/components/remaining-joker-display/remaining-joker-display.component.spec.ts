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
    fixture.detectChanges()
  })

  it('should create', () => {
    expect(component).toBeTruthy()
  })
})
