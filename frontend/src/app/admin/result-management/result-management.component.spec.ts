// noinspection DuplicatedCode

import {ComponentFixture, TestBed} from '@angular/core/testing'

import {ResultManagementComponent} from './result-management.component'
import {MockStore, provideMockStore} from '@ngrx/store/testing'
import {provideRouter} from '@angular/router'
import {hasActiveGroup, selectGroups} from '../../shared/store/tournament.feature'

describe('ResultManagementComponent', () => {
  let component: ResultManagementComponent
  let fixture: ComponentFixture<ResultManagementComponent>
  let store: MockStore

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [ResultManagementComponent],
      providers: [provideMockStore(), provideRouter([])]
    }).compileComponents()

    fixture = TestBed.createComponent(ResultManagementComponent)
    component = fixture.componentInstance
    store = TestBed.inject(MockStore)
    store.overrideSelector(hasActiveGroup, true)
    store.overrideSelector(selectGroups, [])
    store.refreshState()
    fixture.detectChanges()
  })

  it('should create', () => {
    expect(component).toBeTruthy()
  })

  it('should load hasActiveGroup from store', () => {
    expect(component.hasGroupSelected()).toBeTrue()

    store.overrideSelector(hasActiveGroup, false)
    store.refreshState()
    fixture.detectChanges()
    expect(component.hasGroupSelected()).toBeFalse()
  })
})
