import {ComponentFixture, TestBed} from '@angular/core/testing'
import {HeaderComponent} from './header.component'
import {NavItemComponent} from './components/nav-item/nav-item.component'
import {UserMenuComponent} from './components/user-menu/user-menu.component'
import {provideRouter} from '@angular/router'

describe('HeaderComponent', () => {
  let component: HeaderComponent
  let fixture: ComponentFixture<HeaderComponent>

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [HeaderComponent, NavItemComponent, UserMenuComponent],
      providers: [provideRouter([])]
    })
      .compileComponents()

    fixture = TestBed.createComponent(HeaderComponent)
    component = fixture.componentInstance
    fixture.detectChanges()
  })

  it('should create', () => {
    expect(component).toBeTruthy()
  })
})
