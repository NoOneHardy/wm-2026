import {createAction, props} from '@ngrx/store'
import {CardGroup} from '../../model/group/card-group'

export const getOverviewGroups = createAction('[Tournament] Get Overview Groups')
export const overviewGroupsLoaded = createAction('[Tournament] Overview Groups Loaded', props<{ groups: CardGroup[] }>())
