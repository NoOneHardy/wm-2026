import {inject, Pipe, PipeTransform} from '@angular/core'
import {DetailedPoints} from '../../model/game/detailed-points'
import {PointService} from '../services/point/point.service'

@Pipe({
  name: 'totalPoints',
  standalone: true
})
export class TotalPointsPipe implements PipeTransform {
  private pointService = inject(PointService)

  transform(data: DetailedPoints | null): number {
    return data ? this.pointService.getTotal(data) : 0
  }
}
