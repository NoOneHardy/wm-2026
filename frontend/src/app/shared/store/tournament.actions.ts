import {createAction, props} from '@ngrx/store'
import {CardGroup} from '../../model/group/card-group'
import {Group} from '../../model/group/group'
import {BetForm} from '../../model/game/bet-form'
import {ScoreForm} from '../../model/game/score-form'
import {Ranking} from '../../model/leaderboard/ranking'

export const getOverviewGroups = createAction('[Tournament] Get Overview Groups')
export const overviewGroupsLoaded = createAction('[Tournament] Overview Groups Loaded', props<{
  groups: CardGroup[]
}>())
export const selectGroup = createAction('[Tournament] Select Group', props<{ groupId: string }>())
export const groupSelected = createAction('[Tournament] Group Selected', props<{ group: Group }>())
export const deselectGroup = createAction('[Tournament] Deselect Group')
export const saveBets = createAction('[Tournament] Save Bets', props<{ groupId: string, bets: BetForm[] }>())
export const betsSaved = createAction('[Tournament] Bets Saved', props<{ group: Group }>())
export const resetSaving = createAction('[Tournament] Reset Saving')

export const grantJDouble = createAction('[Joker] Grant double joker')
export const revokeJDouble = createAction('[Joker] Revoke double joker')
export const grantJTriple = createAction('[Joker] Grant triple joker')
export const revokeJTriple = createAction('[Joker] Revoke triple joker')

export const saveResults = createAction('[Tournament Admin] Save results', props<{
  groupId: string,
  results: ScoreForm[]
}>())
export const resultsSaved = createAction('[Tournament Admin] Results saved', props<{ group: Group }>())

export const getLeaderboard = createAction('[Tournament] Get Leaderboard')
export const leaderboardLoaded = createAction('[Tournament] Leaderboard Loaded', props<{
  leaderboard: Ranking[]
}>())
