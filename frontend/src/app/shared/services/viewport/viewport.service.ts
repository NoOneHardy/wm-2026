import {computed, HostListener, Injectable, Signal, signal} from '@angular/core'

@Injectable({
  providedIn: 'root'
})
export class ViewportService {
  // private xs = 576
  // private s = 768
  private m = 992
  // private l = 1200
  // private xl = 1400

  viewportWidth = signal(window.innerWidth)

  @HostListener('window:resize')
  onResize(): void {
    this.viewportWidth.set(window.innerWidth)
  }

  // public isVpXSorLess: Signal<boolean> = computed(() => this.viewportWidth() <= this.xs)
  // public isVpSorLess: Signal<boolean> = computed(() => this.viewportWidth() <= this.s)
  public isVPMorSmaller: Signal<boolean> = computed(() => this.viewportWidth() <= this.m)
  // public isVpLorLess: Signal<boolean> = computed(() => this.viewportWidth() <= this.l)
  // public isVpXLorLess: Signal<boolean> = computed(() => this.viewportWidth() <= this.xl)
  // public isVpXXLorLess: Signal<boolean> = computed(() => this.viewportWidth() > this.xl)
}
