import {createAction, props} from '@ngrx/store'
import {CardGroup} from '../../model/group/card-group'
import {Group} from '../../model/group/group'
import {BetForm} from '../../model/game/bet-form'

export const getOverviewGroups = createAction('[Tournament] Get Overview Groups')
export const overviewGroupsLoaded = createAction('[Tournament] Overview Groups Loaded', props<{
  groups: CardGroup[]
}>())
export const selectGroup = createAction('[Tournament] Select Group', props<{ groupId: string }>())
export const groupSelected = createAction('[Tournament] Group Selected', props<{ group: Group }>())
export const saveBets = createAction('[Tournament] Save Bets', props<{ groupId: string, bets: BetForm[] }>())
export const betsSaved = createAction('[Tournament] Bets Saved', props<{group: Group}>())
