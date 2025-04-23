import {createFeature, createReducer, on} from '@ngrx/store'
import {CardGroup} from '../../model/group/card-group'
import {
  betsSaved,
  getOverviewGroups,
  groupSelected,
  overviewGroupsLoaded,
  saveBets,
  selectGroup
} from './tournament.actions'
import {Group} from '../../model/group/group'

interface TournamentState {
  isTournamentLoading: boolean
  isTournamentSaving: boolean
  groups: CardGroup[]
  activeGroup: Group | null
}

const initialState: TournamentState = {
  isTournamentLoading: false,
  isTournamentSaving: false,
  groups: [],
  activeGroup: null
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
    on(saveBets, (state): TournamentState => {
      return {
        ...state,
        isTournamentSaving: true
      }
    }),
    on(betsSaved, (state, action): TournamentState => {
      return {
        ...state,
        isTournamentSaving: false,
        activeGroup: action.group
      }
    }),
    on(overviewGroupsLoaded, (state, action): TournamentState => {
      return {
        ...state,
        isTournamentLoading: false,
        groups: action.groups
      }
    }),
    on(groupSelected, (state, action): TournamentState => {
      return {
        ...state,
        isTournamentLoading: false,
        activeGroup: action.group
      }
    })
  )
})

export const {
  selectIsTournamentLoading,
  selectGroups,
  selectActiveGroup,
  selectIsTournamentSaving
} = tournamentFeature
