import {ScoreForm} from './score-form'

export interface BetForm extends ScoreForm {
  joker: 1 | 2 | 3
}
