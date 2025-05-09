import {ComponentFixture, TestBed} from '@angular/core/testing'

import {BetManagementComponent} from './bet-management.component'

describe('BetManagementComponent', () => {
  let component: BetManagementComponent
  let fixture: ComponentFixture<BetManagementComponent>

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [BetManagementComponent]
    }).compileComponents()

    fixture = TestBed.createComponent(BetManagementComponent)
    component = fixture.componentInstance
    fixture.detectChanges()
  })

  it('should create', () => {
    expect(component).toBeTruthy()
  })
})
