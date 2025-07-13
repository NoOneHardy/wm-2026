import {ComponentFixture, TestBed} from '@angular/core/testing'

import {UpcomingGamesComponent} from './upcoming-games.component'
import {provideRouter} from '@angular/router'

describe('UpcomingGamesComponent', () => {
  let component: UpcomingGamesComponent
  let fixture: ComponentFixture<UpcomingGamesComponent>

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [UpcomingGamesComponent],
      providers: [provideRouter([])]
    }).compileComponents()

    fixture = TestBed.createComponent(UpcomingGamesComponent)
    component = fixture.componentInstance
    fixture.componentRef.setInput('games', [])
    fixture.detectChanges()
  })

  it('should create', () => {
    expect(component).toBeTruthy()
  })
})
