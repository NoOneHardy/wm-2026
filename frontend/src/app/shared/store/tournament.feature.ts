import {createFeature, createReducer, on} from '@ngrx/store'
import {CardGroup} from '../../model/group/card-group'
import {getOverviewGroups, groupSelected, overviewGroupsLoaded, selectGroup} from './tournament.actions'
import {Group} from '../../model/group/group'

interface TournamentState {
  isTournamentLoading: boolean
  groups: CardGroup[]
  activeGroup: Group | null
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
    on(
      getOverviewGroups,
      selectGroup,
      (state): TournamentState => {
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
  selectActiveGroup
} = tournamentFeature
