import {FeatureSlice} from '@ngrx/store'
import * as feature from './home.feature'
import {HomeState} from './home.feature'
import {homeDataLoaded, homeDataLoadFailed, loadHomeData} from './home.actions'

describe('HomeFeature', () => {
  let store: FeatureSlice<HomeState>
  let initialState: HomeState

  beforeEach(() => {
    store = feature.homeFeature
    initialState = feature.initialState
  })

  it('should initialize', () => {
    expect(store).toBeTruthy()
  })

  it('should set isHomeLoading to true when loading data', () => {
    let state = initialState
    expect(state.isHomeLoading).toBe(false)
    state = store.reducer(state, loadHomeData())
    expect(state.isHomeLoading).toBe(true)
  })

  it('should store data and stop loading when data has been loaded', () => {
    let state = store.reducer(initialState, loadHomeData())
    expect(state.isHomeLoading).toBe(true)
    state = store.reducer(state, homeDataLoaded({
      data: {
        jackpot: 210,
        players: 42,
        games: 35
      }
    }))
    expect(state.isHomeLoading).toBe(false)
    expect(state.homeData).toEqual({
      jackpot: 210,
      players: 42,
      games: 35
    })
  })

  it('should stop loading when loading data fails', () => {
    let state = store.reducer(initialState, loadHomeData())
    expect(state.isHomeLoading).toBe(true)
    state = store.reducer(state, homeDataLoadFailed())
    expect(state.isHomeLoading).toBe(false)
  })
})
