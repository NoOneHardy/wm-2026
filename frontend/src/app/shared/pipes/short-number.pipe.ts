import {Pipe, PipeTransform} from '@angular/core'

@Pipe({
  name: 'shortNumber',
  standalone: true
})
export class ShortNumberPipe implements PipeTransform {
  transform(value: number): string {
    if (value < 1000) return value.toString()

    const frac = value / 1000
    const rounded = this.getRounded(frac)

    const fixed = rounded.toFixed(3).slice(0, 4)
    if (fixed.endsWith('.')) return fixed.slice(0, 3) + 'k'
    return fixed + 'k'
  }

  private getRounded(frac: number): number {
    if (frac < 10) return Math.round(frac * 100) / 100
    if (frac < 100) return Math.round(frac * 10) / 10
    return Math.round(frac)
  }
}
