import {createFeature, createReducer, on} from '@ngrx/store'
import {CardGroup} from '../../model/group/card-group'
import {getOverviewGroups, overviewGroupsLoaded} from './tournament.actions'

interface TournamentState {
  isTournamentLoading: boolean
  groups: CardGroup[]
  activeGroup: CardGroup | null
}

const initialState: TournamentState = {
  isTournamentLoading: false,
  groups: [],
  activeGroup: null
}

export const tournamentFeature = createFeature({
  name: 'tournament',
  reducer: createReducer(
    initialState,
    on(getOverviewGroups, (state): TournamentState => {
      return {
        ...state,
        isTournamentLoading: true
      }
    }),
    on(overviewGroupsLoaded, (state, action): TournamentState => {
      return {
        ...state,
        isTournamentLoading: false,
        groups: action.groups
      }
    })
  )
})

export const {
  selectIsTournamentLoading,
  selectGroups
} = tournamentFeature
