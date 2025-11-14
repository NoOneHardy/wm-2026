import {ComponentFixture, TestBed} from '@angular/core/testing'

import {SideNavItemComponent} from './side-nav-item.component'
import {provideRouter} from '@angular/router'

describe('SideNavItemComponent', () => {
  let component: SideNavItemComponent
  let fixture: ComponentFixture<SideNavItemComponent>

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [SideNavItemComponent],
      providers: [provideRouter([])],
    }).compileComponents()

    fixture = TestBed.createComponent(SideNavItemComponent)
    component = fixture.componentInstance
    fixture.detectChanges()
  })

  it('should create', () => {
    expect(component).toBeTruthy()
  })
})
