import { ComponentFixture, TestBed } from '@angular/core/testing'

import { SignupComponent } from './signup.component'
import {provideHttpClient} from '@angular/common/http'

describe('SignupComponent', () => {
  let component: SignupComponent
  let fixture: ComponentFixture<SignupComponent>

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [SignupComponent],
      providers: [provideHttpClient()]
    })
    .compileComponents()

    fixture = TestBed.createComponent(SignupComponent)
    component = fixture.componentInstance
    fixture.detectChanges()
  })

  it('should create', () => {
    expect(component).toBeTruthy()
  })
})
