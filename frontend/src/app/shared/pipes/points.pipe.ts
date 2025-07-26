import {inject, Pipe, PipeTransform} from '@angular/core'
import {BetGame} from '../../model/game/bet-game'
import {DetailedPoints} from '../../model/game/detailed-points'
import {PointService} from '../services/point/point.service'

@Pipe({
  name: 'points',
  standalone: true
})
export class PointsPipe implements PipeTransform {
  private pointService = inject(PointService)

  transform(game: BetGame): DetailedPoints | null {
    return this.pointService.calculateDetailedPoints(game)
  }

}
