import { ComponentFixture, TestBed } from '@angular/core/testing'

import { SnackbarDisplayComponent } from './snackbar-display.component'

describe('SnackbarDisplayComponent', () => {
  let component: SnackbarDisplayComponent
  let fixture: ComponentFixture<SnackbarDisplayComponent>

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [SnackbarDisplayComponent]
    })
    .compileComponents()

    fixture = TestBed.createComponent(SnackbarDisplayComponent)
    component = fixture.componentInstance
    fixture.detectChanges()
  })

  it('should create', () => {
    expect(component).toBeTruthy()
  })
})
