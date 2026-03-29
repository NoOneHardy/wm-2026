import { ComponentFixture, TestBed } from '@angular/core/testing'

import { PayoutBoardComponent } from './payout-board.component'

describe('PayoutBoardComponent', () => {
  let component: PayoutBoardComponent
  let fixture: ComponentFixture<PayoutBoardComponent>

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [PayoutBoardComponent]
    })
    .compileComponents()

    fixture = TestBed.createComponent(PayoutBoardComponent)
    component = fixture.componentInstance
    fixture.detectChanges()
  })

  it('should create', () => {
    expect(component).toBeTruthy()
  })
})
