import {ComponentFixture, TestBed} from '@angular/core/testing'
import {vi} from 'vitest'
import {HomeComponent} from './home.component'
import {provideRouter} from '@angular/router'
import {MockStore, provideMockStore} from '@ngrx/store/testing'
import {loadHomeData} from './store/home.actions'
import {selectHomeData} from './store/home.feature'

describe('HomeComponent', () => {
  let component: HomeComponent
  let fixture: ComponentFixture<HomeComponent>
  let store: MockStore

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [HomeComponent],
      providers: [provideMockStore(), provideRouter([])]
    }).compileComponents()

    store = TestBed.inject(MockStore)
    store.overrideSelector(selectHomeData, {
      jackpot: 210,
      players: 42,
      games: 35
    })
    fixture = TestBed.createComponent(HomeComponent)
    component = fixture.componentInstance
    vi.spyOn(store, 'dispatch')
    fixture.detectChanges()
  })

  it('should create', () => {
    expect(component).toBeTruthy()
  })

  it('should load home data on init', () => {
    expect(store.dispatch).toHaveBeenCalledExactlyOnceWith(loadHomeData())
  })

  it('should expose formatted stats from the store', () => {
    expect(component.stats()).toEqual({
      jackpot: 'CHF 210',
      players: '42',
      games: '35'
    })
  })
})
