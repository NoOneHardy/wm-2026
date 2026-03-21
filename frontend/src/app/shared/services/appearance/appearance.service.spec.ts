import {TestBed} from '@angular/core/testing'

import {AppearanceService} from './appearance.service'

describe('AppearanceService', () => {
  beforeEach(() => {
    localStorage.removeItem('wm-appearance-theme')
    document.documentElement.removeAttribute('data-wm-theme')
    document.documentElement.style.colorScheme = ''
    TestBed.configureTestingModule({})
  })

  afterEach(() => {
    localStorage.removeItem('wm-appearance-theme')
    document.documentElement.removeAttribute('data-wm-theme')
    document.documentElement.style.colorScheme = ''
  })

  it('should be created', () => {
    const service = TestBed.inject(AppearanceService)

    expect(service).toBeTruthy()
  })

  it('should load a stored theme and apply it to the document root', () => {
    localStorage.setItem('wm-appearance-theme', 'dark')

    const service = TestBed.inject(AppearanceService)
    TestBed.flushEffects()

    expect(service.theme()).toBe('dark')
    expect(document.documentElement.getAttribute('data-wm-theme')).toBe('dark')
    expect(document.documentElement.style.colorScheme).toBe('dark')
  })

  it('should persist theme changes', () => {
    const service = TestBed.inject(AppearanceService)

    service.setTheme('dark')
    TestBed.flushEffects()

    expect(localStorage.getItem('wm-appearance-theme')).toBe('dark')
    expect(document.documentElement.getAttribute('data-wm-theme')).toBe('dark')
    expect(document.documentElement.style.colorScheme).toBe('dark')
  })
})
