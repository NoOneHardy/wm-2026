import {ComponentFixture, TestBed} from '@angular/core/testing'

import {RecentResultsComponent} from './recent-results.component'
import {provideRouter} from '@angular/router'

describe('RecentResultsComponent', () => {
  let component: RecentResultsComponent
  let fixture: ComponentFixture<RecentResultsComponent>

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [RecentResultsComponent],
      providers: [provideRouter([])]
    }).compileComponents()

    fixture = TestBed.createComponent(RecentResultsComponent)
    component = fixture.componentInstance
    fixture.componentRef.setInput('games', [])
    fixture.detectChanges()
  })

  it('should create', () => {
    expect(component).toBeTruthy()
  })
})
