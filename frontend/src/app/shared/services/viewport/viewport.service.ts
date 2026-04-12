import {computed, Injectable, Signal, signal} from '@angular/core'

@Injectable({
  providedIn: 'root'
})
export class ViewportService {
  // private readonly xs = 576
  // private readonly s = 768
  private readonly m = 992
  // private readonly l = 1200
  // private readonly xl = 1400

  viewportWidth = signal(window.innerWidth)

  constructor() {
    window.addEventListener('resize', () => {
      this.viewportWidth.set(window.innerWidth)
    })
  }

  // public isVpXSorLess: Signal<boolean> = computed(() => this.viewportWidth() <= this.xs)
  // public isVpSorLess: Signal<boolean> = computed(() => this.viewportWidth() <= this.s)
  public isVPMorSmaller: Signal<boolean> = computed(() => this.viewportWidth() <= this.m)
  // public isVpLorLess: Signal<boolean> = computed(() => this.viewportWidth() <= this.l)
  // public isVpXLorLess: Signal<boolean> = computed(() => this.viewportWidth() <= this.xl)
  // public isVpXXLorLess: Signal<boolean> = computed(() => this.viewportWidth() > this.xl)
}
