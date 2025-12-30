import {ComponentFixture, TestBed} from '@angular/core/testing'
import {PasswordResetComponent} from './password-reset.component'
import {ActivatedRoute, provideRouter, Router} from '@angular/router'
import {provideMockStore} from '@ngrx/store/testing'

describe('PasswordResetComponent', () => {
  let component: PasswordResetComponent
  let fixture: ComponentFixture<PasswordResetComponent>

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [PasswordResetComponent],
      providers: [provideRouter([]), provideMockStore(), {
        provide: ActivatedRoute,
        useValue: {
          snapshot: {
            queryParamMap: {
              get: () => {
                return 'test-code'
              }
            }
          }
        }
      }]
    }).compileComponents()

    fixture = TestBed.createComponent(PasswordResetComponent)
    component = fixture.componentInstance
    fixture.detectChanges()
  })

  it('should create', () => {
    expect(component).toBeTruthy()
  })

  it('should set code on init', () => {
    expect(component['code']).toBe('test-code')
  })

  it('should not set code and navigate away if no code is present', () => {
    const router = TestBed.inject(Router)
    const route = TestBed.inject(ActivatedRoute)
    spyOn(router, 'navigateByUrl')
    spyOn(route.snapshot.queryParamMap, 'get').and.returnValue(null)

    fixture = TestBed.createComponent(PasswordResetComponent)
    component = fixture.componentInstance

    component.ngOnInit()
    expect(component['code']).toBe('')
    expect(router.navigateByUrl).toHaveBeenCalledWith('/')
  })
})
