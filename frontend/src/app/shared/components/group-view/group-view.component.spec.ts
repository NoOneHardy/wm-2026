import {ComponentFixture, TestBed} from '@angular/core/testing'

import {GroupViewComponent} from './group-view.component'
import {provideMockStore} from '@ngrx/store/testing'
import {provideRouter} from '@angular/router'

describe('GroupViewComponent', () => {
  let component: GroupViewComponent
  let fixture: ComponentFixture<GroupViewComponent>

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [GroupViewComponent],
      providers: [
        provideMockStore(),
        provideRouter([])
      ]
    }).compileComponents()

    fixture = TestBed.createComponent(GroupViewComponent)
    component = fixture.componentInstance
    fixture.detectChanges()
  })

  it('should create', () => {
    expect(component).toBeTruthy()
  })
})
