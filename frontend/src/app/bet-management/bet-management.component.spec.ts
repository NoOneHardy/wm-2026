// noinspection DuplicatedCode

import {ComponentFixture, TestBed} from '@angular/core/testing'

import {BetManagementComponent} from './bet-management.component'
import {MockStore, provideMockStore} from '@ngrx/store/testing'
import {hasActiveGroup, selectGroups} from '../shared/store/tournament.feature'
import {provideRouter} from '@angular/router'

describe('BetManagementComponent', () => {
  let component: BetManagementComponent
  let fixture: ComponentFixture<BetManagementComponent>
  let store: MockStore

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [BetManagementComponent],
      providers: [provideMockStore(), provideRouter([])]
    }).compileComponents()

    fixture = TestBed.createComponent(BetManagementComponent)
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
