import {ComponentFixture, TestBed} from '@angular/core/testing'

import {StatCardComponent} from './stat-card.component'

describe('StatCardComponent', () => {
  let component: StatCardComponent
  let fixture: ComponentFixture<StatCardComponent>

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [StatCardComponent]
    }).compileComponents()

    fixture = TestBed.createComponent(StatCardComponent)
    component = fixture.componentInstance
    fixture.componentRef.setInput('icon', 'stars')
    fixture.componentRef.setInput('label', 'Punkte')
    fixture.detectChanges()
  })

  it('should create', () => {
    expect(component).toBeTruthy()
  })

  it('should render the icon and label', () => {
    const element: HTMLElement = fixture.nativeElement
    expect(element.querySelector('.icon span')?.textContent).toContain('stars')
    expect(element.querySelector('.label')?.textContent).toContain('Punkte')
  })
})
