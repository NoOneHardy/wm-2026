import {TestBed} from '@angular/core/testing'
import {AppComponent} from './app.component'
import {HeaderComponent} from './shared/components/header/header.component'
import {provideRouter, RouterLinkActive, RouterOutlet} from '@angular/router'
import {provideMockStore} from '@ngrx/store/testing'
import {selectGroups} from './shared/store/tournament.feature'
import {provideAnimations} from '@angular/platform-browser/animations'

describe('AppComponent', () => {
  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [AppComponent, HeaderComponent, RouterOutlet, RouterLinkActive],
      providers: [provideRouter([]), provideMockStore({
        selectors: [
          {selector: selectGroups, value: []}
        ]
      }), provideAnimations()],
    }).compileComponents()
  })

  it('should create the app', () => {
    const fixture = TestBed.createComponent(AppComponent)
    const app = fixture.componentInstance
    expect(app).toBeTruthy()
  })
})
