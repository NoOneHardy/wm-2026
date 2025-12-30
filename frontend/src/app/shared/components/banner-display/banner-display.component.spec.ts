import {ComponentFixture, TestBed} from '@angular/core/testing'
import {BannerDisplayComponent} from './banner-display.component'
import {provideMockStore} from '@ngrx/store/testing'

describe('BannerDisplayComponent', () => {
  let component: BannerDisplayComponent
  let fixture: ComponentFixture<BannerDisplayComponent>

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [BannerDisplayComponent],
      providers: [provideMockStore()]
    }).compileComponents()

    fixture = TestBed.createComponent(BannerDisplayComponent)
    component = fixture.componentInstance
    fixture.detectChanges()
  })

  it('should create', () => {
    expect(component).toBeTruthy()
  })
})
