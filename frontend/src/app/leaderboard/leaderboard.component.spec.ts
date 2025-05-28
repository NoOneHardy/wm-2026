import {ComponentFixture, TestBed} from '@angular/core/testing'

import {LeaderboardComponent} from './leaderboard.component'
import {MockStore, provideMockStore} from '@ngrx/store/testing'
import {selectLeaderboard} from '../shared/store/tournament.feature'

describe('LeaderboardComponent', () => {
  let component: LeaderboardComponent
  let fixture: ComponentFixture<LeaderboardComponent>
  let store: MockStore

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [LeaderboardComponent],
      providers: [provideMockStore()]
    }).compileComponents()

    store = TestBed.inject(MockStore)
    store.overrideSelector(selectLeaderboard, [])
    store.refreshState()
    fixture = TestBed.createComponent(LeaderboardComponent)
    component = fixture.componentInstance
    fixture.detectChanges()
  })

  it('should create', () => {
    expect(component).toBeTruthy()
  })
})
