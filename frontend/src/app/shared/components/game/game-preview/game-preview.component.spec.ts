import { ComponentFixture, TestBed } from '@angular/core/testing'

import { GamePreviewComponent } from './game-preview.component'

describe('GamePreviewComponent', () => {
  let component: GamePreviewComponent
  let fixture: ComponentFixture<GamePreviewComponent>

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [GamePreviewComponent]
    })
    .compileComponents()

    fixture = TestBed.createComponent(GamePreviewComponent)
    component = fixture.componentInstance
    fixture.detectChanges()
  })

  it('should create', () => {
    expect(component).toBeTruthy()
  })
})
