import {ComponentFixture, TestBed} from '@angular/core/testing'

import {OverviewComponent} from './overview.component'
import {MockStore, provideMockStore} from '@ngrx/store/testing'
import {selectGroups, selectPercentage, selectPercentageResult} from '../../store/tournament.feature'
import {CardGroup} from '../../../model/group/card-group'
import {provideRouter} from '@angular/router'

describe('OverviewComponent', () => {
  let mockStore: MockStore
  let component: OverviewComponent
  let fixture: ComponentFixture<OverviewComponent>

  const loadMockGroups = (groups: CardGroup[] = [
    {
      name: 'Group B',
      percentage: 60,
      isKnockout: false,
      percentageResult: 33,
      id: 'group-b',
      order: 0,
      thumbnail: []
    },
    {
      name: 'Group A',
      percentage: 50,
      isKnockout: false,
      percentageResult: 0,
      id: 'group-a',
      order: 0,
      thumbnail: []
    },
    {
      name: 'Final',
      percentage: 0,
      isKnockout: true,
      percentageResult: 100,
      id: 'final',
      order: 1,
      thumbnail: []
    },
    {
      name: 'Semi-Final',
      percentage: 20,
      isKnockout: true,
      percentageResult: 0,
      id: 'semi-final',
      order: 0,
      thumbnail: []
    },
  ]) => {
    mockStore.overrideSelector(selectGroups, groups)
    mockStore.overrideSelector(selectPercentageResult, 33.25)
    mockStore.overrideSelector(selectPercentage, 32.5)
    mockStore.refreshState()
    fixture.detectChanges()
  }

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [OverviewComponent],
      providers: [provideMockStore({
        selectors: [
          {selector: selectGroups, value: []}
        ]
      }), provideRouter([])]
    })
      .compileComponents()

    mockStore = TestBed.inject(MockStore)
    fixture = TestBed.createComponent(OverviewComponent)
    component = fixture.componentInstance
    fixture.detectChanges()
  })

  it('should create', () => {
    expect(component).toBeTruthy()
  })

  it('should default totalPercentage to 0', () => {
    mockStore.overrideSelector(selectPercentage, 0)
    mockStore.refreshState()
    const totalPercentage = component.totalPercentage()
    expect(totalPercentage).toBe(0)
  })

  it('should return percentage of bets for bet mode', () => {
    loadMockGroups()
    expect(component.totalPercentage()).toBe(32.5)
  })

  it('should return percentage of results for admin mode', () => {
    loadMockGroups()
    fixture.componentRef.setInput('mode', 'admin')

    expect(component.totalPercentage()).toBe(33.25)
  })

  it('should separate knockout groups', () => {
    loadMockGroups()
    expect(component.knockoutGroups()).toHaveSize(2)
    expect(component.defaultGroups()).toHaveSize(2)
  })

  it('should sort default groups', () => {
    loadMockGroups()
    expect(component.defaultGroups()[0].id).toBe('group-a')
    expect(component.defaultGroups()[1].id).toBe('group-b')
  })

  it('should sort knockout groups', () => {
    loadMockGroups()
    expect(component.knockoutGroups()[0].id).toBe('semi-final')
    expect(component.knockoutGroups()[1].id).toBe('final')
  })
})
