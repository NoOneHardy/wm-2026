import {Component} from '@angular/core'
import {ComponentFixture, TestBed} from '@angular/core/testing'

@Component({
  imports: [],
  template: ''
})
export class TestHostComponent {

}

export async function configureTestHost(config: Partial<Component>): Promise<[
  ComponentFixture<TestHostComponent>,
  TestHostComponent
]> {
  TestBed.resetTestingModule()
  await TestBed.overrideComponent(TestHostComponent, {
    set: config
  }).configureTestingModule({
    imports: [TestHostComponent]
  }).compileComponents()

  const fixture: ComponentFixture<TestHostComponent> = TestBed.createComponent(TestHostComponent)
  const component = fixture.componentInstance
  fixture.detectChanges()
  await fixture.whenRenderingDone()
  fixture.detectChanges()
  return [fixture, component]
}
