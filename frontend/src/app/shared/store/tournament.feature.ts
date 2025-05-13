import {createFeature, createReducer, createSelector, on} from '@ngrx/store'
import {CardGroup} from '../../model/group/card-group'
import {
  betsSaved, deselectGroup,
  getOverviewGroups, grantJDouble, grantJTriple,
  groupSelected,
  overviewGroupsLoaded, resetSaving, resultsSaved,
  revokeJDouble, revokeJTriple,
  saveBets, saveResults,
  selectGroup
} from './tournament.actions'
import {Group} from '../../model/group/group'
import {AvailableJokers} from '../../model/group/available-jokers'

export interface TournamentState {
  isTournamentLoading: boolean
  isTournamentSaving: boolean
  groups: CardGroup[]
  activeGroup: Group | null
  availableJokers: AvailableJokers | null
}

export const initialState: TournamentState = {
  isTournamentLoading: false,
  isTournamentSaving: false,
  groups: [],
  activeGroup: null,
  availableJokers: null
}

export const tournamentFeature = createFeature({
  name: 'tournament',
  reducer: createReducer(
    initialState,
    on(
      getOverviewGroups,
      selectGroup,
      (state): TournamentState => {
        return {
          ...state,
          isTournamentLoading: true
        }
      }),
    on(saveBets, saveResults, (state): TournamentState => {
      return {
        ...state,
        isTournamentSaving: true
      }
    }),
    on(resetSaving, (state): TournamentState => {
      return {
        ...state,
        isTournamentSaving: false
      }
    }),
    on(betsSaved, resultsSaved, (state, action): TournamentState => {
      return {
        ...state,
        isTournamentSaving: false,
        activeGroup: action.group,
        availableJokers: action.group.availableJokers
      }
    }),
    on(overviewGroupsLoaded, (state, action): TournamentState => {
      return {
        ...state,
        isTournamentLoading: false,
        groups: action.groups,
        activeGroup: null
      }
    }),
    on(deselectGroup, (state): TournamentState => {
      return {
        ...state,
        activeGroup: null
      }
    }),
    on(groupSelected, (state, action): TournamentState => {
      return {
        ...state,
        isTournamentLoading: false,
        activeGroup: action.group,
        availableJokers: action.group.availableJokers
      }
    }),
    on(grantJDouble, (state): TournamentState => {
      const jokers = state.availableJokers
      if (!jokers) return state
      return {
        ...state,
        availableJokers: {
          ...jokers,
          jdouble: jokers.jdouble + 1,
        }
      }
    }),
    on(revokeJDouble, (state): TournamentState => {
      const jokers = state.availableJokers
      if (!jokers) return state
      return {
        ...state,
        availableJokers: {
          ...jokers,
          jdouble: jokers.jdouble - 1
        }
      }
    }),
    on(grantJTriple, (state): TournamentState => {
      const jokers = state.availableJokers
      if (!jokers) return state
      return {
        ...state,
        availableJokers: {
          ...jokers,
          jtriple: jokers.jtriple + 1
        }
      }
    }),
    on(revokeJTriple, (state): TournamentState => {
      const jokers = state.availableJokers
      if (!jokers) return state
      return {
        ...state,
        availableJokers: {
          ...jokers,
          jtriple: jokers.jtriple - 1
        }
      }
    })
  )
})

export const {
  selectIsTournamentLoading,
  selectGroups,
  selectActiveGroup,
  selectIsTournamentSaving,
  selectAvailableJokers
} = tournamentFeature

export const hasActiveGroup = createSelector(
  selectActiveGroup,
  (group): boolean => !!group
)
