import {ComponentFixture, TestBed} from '@angular/core/testing'
import {NavItemComponent} from './nav-item.component'
import {provideRouter, RouterLinkActive} from '@angular/router'

describe('NavItemComponent', () => {
  let component: NavItemComponent
  let fixture: ComponentFixture<NavItemComponent>

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [NavItemComponent, RouterLinkActive],
      providers: [provideRouter([])]
    })
    .compileComponents()

    fixture = TestBed.createComponent(NavItemComponent)
    component = fixture.componentInstance
    fixture.detectChanges()
  })

  it('should create', () => {
    expect(component).toBeTruthy()
  })
})
