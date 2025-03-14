import { ComponentFixture, TestBed } from '@angular/core/testing'

import { ScoreFormFieldComponent } from './score-form-field.component'

describe('ScoreFormFieldComponent', () => {
  let component: ScoreFormFieldComponent
  let fixture: ComponentFixture<ScoreFormFieldComponent>

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [ScoreFormFieldComponent]
    })
    .compileComponents()

    fixture = TestBed.createComponent(ScoreFormFieldComponent)
    component = fixture.componentInstance
    fixture.detectChanges()
  })

  it('should create', () => {
    expect(component).toBeTruthy()
  })
})
