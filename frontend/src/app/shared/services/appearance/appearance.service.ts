import {DOCUMENT} from '@angular/common'
import {effect, inject, Injectable, Signal, signal} from '@angular/core'

export type AppearanceTheme = 'light' | 'dark'

const STORAGE_KEY = 'wm-appearance-theme'

@Injectable({
  providedIn: 'root'
})
export class AppearanceService {
  private document = inject(DOCUMENT)
  private themeState = signal<AppearanceTheme>(this.getInitialTheme())

  readonly theme: Signal<AppearanceTheme> = this.themeState.asReadonly()

  constructor() {
    effect(() => {
      const theme = this.themeState()
      this.document.documentElement.setAttribute('data-wm-theme', theme)
      this.document.documentElement.style.colorScheme = theme
      this.persistTheme(theme)
    })
  }

  initialize(): void {
  }

  setTheme(theme: AppearanceTheme): void {
    this.themeState.set(theme)
  }

  private getInitialTheme(): AppearanceTheme {
    const storedTheme = this.getStoredTheme()
    if (storedTheme) return storedTheme

    return this.prefersDarkMode() ? 'dark' : 'light'
  }

  private getStoredTheme(): AppearanceTheme | null {
    try {
      const theme = window.localStorage.getItem(STORAGE_KEY)
      return theme === 'light' || theme === 'dark' ? theme : null
    } catch {
      return null
    }
  }

  private persistTheme(theme: AppearanceTheme): void {
    try {
      window.localStorage.setItem(STORAGE_KEY, theme)
    } catch {
      // Ignore storage errors so theme switching still works for the session.
    }
  }

  private prefersDarkMode(): boolean {
    return typeof window !== 'undefined'
      && typeof window.matchMedia === 'function'
      && window.matchMedia('(prefers-color-scheme: dark)').matches
  }
}
