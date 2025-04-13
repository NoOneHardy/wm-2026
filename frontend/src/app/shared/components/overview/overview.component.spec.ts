import {ComponentFixture, TestBed} from '@angular/core/testing'

import {OverviewComponent} from './overview.component'
import {provideMockStore} from '@ngrx/store/testing'
import {selectGroups} from '../../store/tournament.feature'

describe('OverviewComponent', () => {
  let component: OverviewComponent
  let fixture: ComponentFixture<OverviewComponent>

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [OverviewComponent],
      providers: [provideMockStore({
        selectors: [
          {selector: selectGroups, value: []}
        ]
      })]
    })
      .compileComponents()

    fixture = TestBed.createComponent(OverviewComponent)
    component = fixture.componentInstance
    fixture.detectChanges()
  })

  it('should create', () => {
    expect(component).toBeTruthy()
  })
})
