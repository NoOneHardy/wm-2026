import {FeatureSlice} from '@ngrx/store'
import * as feature from './tournament.feature'
import {TournamentState} from './tournament.feature'
import {
  grantJDouble,
  grantJTriple,
  groupLoaded,
  loadGroup,
  overviewGroupsLoaded,
  resultsSaved,
  revokeJDouble,
  revokeJTriple,
  saveResults
} from './tournament.actions'
import {Group} from '../../model/group/group'

const mockGroup: Group = {
  id: 'group-1',
  name: 'Gruppe A',
  games: [],
  isKnockout: false,
  lastSavedAt: new Date(),
  lastSavedAtResult: new Date(),
  percentage: 100,
  percentageResult: 100,
  availableJokers: {
    jdouble: 2,
    jtriple: 3,
    jdoubleMax: 10,
    jtripleMax: 6
  }
}

describe('TournamentFeature', () => {
  let store: FeatureSlice<TournamentState>
  let initialState: TournamentState

  beforeEach(() => {
    store = feature.tournamentFeature
    initialState = feature.initialState
  })

  it('should initialize', () => {
    expect(store).toBeTruthy()
  })

  it('should set available jokers when setting active group', () => {
    let availableJokers = initialState.availableJokers
    expect(availableJokers).toBeFalsy()
    availableJokers = store.reducer(initialState, groupLoaded({group: mockGroup})).availableJokers
    expect(availableJokers).toBeTruthy()
  })

  it('should add a double joker', () => {
    let state = store.reducer(initialState, groupLoaded({group: mockGroup}))
    let availableJokers = state.availableJokers
    expect(availableJokers?.jdouble).toBe(2)
    state = store.reducer(state, grantJDouble())
    availableJokers = state.availableJokers
    expect(availableJokers?.jdouble).toBe(3)
  })

  it('should remove a double joker', () => {
    let state = store.reducer(initialState, groupLoaded({group: mockGroup}))
    let availableJokers = state.availableJokers
    expect(availableJokers?.jdouble).toBe(2)
    state = store.reducer(state, revokeJDouble())
    availableJokers = state.availableJokers
    expect(availableJokers?.jdouble).toBe(1)
  })

  it('should add a triple joker', () => {
    let state = store.reducer(initialState, groupLoaded({group: mockGroup}))
    let availableJokers = state.availableJokers
    expect(availableJokers?.jtriple).toBe(3)
    state = store.reducer(state, grantJTriple())
    availableJokers = state.availableJokers
    expect(availableJokers?.jtriple).toBe(4)
  })

  it('should remove a triple joker', () => {
    let state = store.reducer(initialState, groupLoaded({group: mockGroup}))
    let availableJokers = state.availableJokers
    expect(availableJokers?.jtriple).toBe(3)
    state = store.reducer(state, revokeJTriple())
    availableJokers = state.availableJokers
    expect(availableJokers?.jtriple).toBe(2)
  })

  it('should start loading when selecting a group', () => {
    let state = initialState
    expect(state.isTournamentLoading).toBe(false)
    state = store.reducer(state, loadGroup({groupId: 'group-1'}))
    expect(state.isTournamentLoading).toBe(true)
  })

  it('should stop loading when group has been loaded', () => {
    let state = initialState
    state = store.reducer(state, loadGroup({groupId: 'group-1'}))
    expect(state.isTournamentLoading).toBe(true)
    state = store.reducer(state, groupLoaded({group: mockGroup}))
    expect(state.isTournamentLoading).toBe(false)
  })

  it('should reset active group when loading overview groups', () => {
    let state = initialState
    state = store.reducer(state, groupLoaded({group: mockGroup}))
    expect(state.activeGroup).toBeTruthy()
    state = store.reducer(state, overviewGroupsLoaded({
      overview: {
        groups: [],
        percentage: 0,
        percentageResult: 0
      }
    }))
    expect(state.activeGroup).toBeNull()
  })

  it('should start loading when saving results', () => {
    let state = initialState
    expect(state.isTournamentSaving).toBe(false)
    state = store.reducer(state, saveResults({groupId: 'group-1', results: []}))
    expect(state.isTournamentSaving).toBe(true)
  })

  it('should stop loading when results have been saved', () => {
    let state = initialState
    state = store.reducer(state, saveResults({groupId: 'group-1', results: []}))
    expect(state.isTournamentSaving).toBe(true)
    state = store.reducer(state, resultsSaved({group: mockGroup}))
    expect(state.isTournamentSaving).toBe(false)
  })

  it('should update group and available jokers when results have been saved', () => {
    let state = initialState
    state = store.reducer(state, saveResults({groupId: 'group-1', results: []}))
    expect(state.isTournamentSaving).toBe(true)
    state = store.reducer(state, resultsSaved({group: mockGroup}))
    expect(state.activeGroup).toEqual(mockGroup)
    expect(state.availableJokers).toEqual(mockGroup.availableJokers)
  })
})
