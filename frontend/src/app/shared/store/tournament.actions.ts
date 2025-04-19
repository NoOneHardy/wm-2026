import {createAction, props} from '@ngrx/store'
import {CardGroup} from '../../model/group/card-group'
import {Group} from '../../model/group/group'

export const getOverviewGroups = createAction('[Tournament] Get Overview Groups')
export const overviewGroupsLoaded = createAction('[Tournament] Overview Groups Loaded', props<{
  groups: CardGroup[]
}>())
export const selectGroup = createAction('[Tournament] Select Group', props<{ groupId: string }>())
export const groupSelected = createAction('[Tournament] Group Selected', props<{ group: Group }>())
