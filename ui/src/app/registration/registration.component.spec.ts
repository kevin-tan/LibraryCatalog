import { TestBed, async } from '@angular/core/testing';
import { RegistrationComponent } from './registration.component';

describe('AppComponent', () => {
  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [
        RegistrationComponent
      ],
    }).compileComponents();
  }));

  it('should create the app', () => {
    const fixture = TestBed.createComponent(RegistrationComponent);
    const app = fixture.debugElement.componentInstance;
    expect(app).toBeTruthy();
  });

  it(`should have as title 'ui'`, () => {
    const fixture = TestBed.createComponent(RegistrationComponent);
    const app = fixture.debugElement.componentInstance;
    expect(app.title).toEqual('ui');
  });

  it('should render title in a h1 tag', () => {
    const fixture = TestBed.createComponent(RegistrationComponent);
    fixture.detectChanges();
    const compiled = fixture.debugElement.nativeElement;
    expect(compiled.querySelector('h1').textContent).toContain('Welcome to ui!');
  });
});
