import { LightTeam } from '../team/light-team'
import {Score} from './score'

export interface PreviousGame {
  id: string
  teamHome: LightTeam
  teamGuest: LightTeam
  result: Score
}
