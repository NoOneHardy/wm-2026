import {createFeature, createReducer, on} from '@ngrx/store'
import {CardGroup} from '../../model/group/card-group'
import {getOverviewGroups, overviewGroupsLoaded} from './tournament.actions'

interface TournamentState {
  isLoading: boolean
  groups: CardGroup[]
  activeGroup: CardGroup | null
}

const initialState: TournamentState = {
  isLoading: false,
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
        isLoading: true
      }
    }),
    on(overviewGroupsLoaded, (state, action): TournamentState => {
      return {
        ...state,
        isLoading: false,
        groups: action.groups
      }
    })
  )
})

export const {
  selectIsLoading,
  selectGroups
} = tournamentFeature
