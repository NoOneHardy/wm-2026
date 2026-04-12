import {LightTeam} from './light-team'
import {PreviousGame} from '../game/previous-game'

export interface Team extends LightTeam {
  previousGames: PreviousGame[]
}
