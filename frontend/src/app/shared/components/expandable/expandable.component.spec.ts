import {ComponentFixture, TestBed} from '@angular/core/testing'

import {ExpandableComponent} from './expandable.component'
import {provideAnimations} from '@angular/platform-browser/animations'

describe('ExpandableComponent', () => {
  let component: ExpandableComponent
  let fixture: ComponentFixture<ExpandableComponent>

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [ExpandableComponent],
      providers: [provideAnimations()]
    }).compileComponents()

    fixture = TestBed.createComponent(ExpandableComponent)
    component = fixture.componentInstance
    fixture.componentRef.setInput('name', 'Test Expandable')
    fixture.detectChanges()
  })

  it('should create', () => {
    expect(component).toBeTruthy()
  })
})
