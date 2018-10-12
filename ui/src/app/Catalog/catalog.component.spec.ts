import { TestBed, async } from '@angular/core/testing';
import { CatalogComponent } from './catalog.component';

describe('CatalogComponent', () => {
  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [
        CatalogComponent
      ],
    }).compileComponents();
  }));

  it('should create the catalog', () => {
    const fixture = TestBed.createComponent(CatalogComponent);
    const app = fixture.debugElement.componentInstance;
    expect(app).toBeTruthy();
  });

  it(`should have as title 'ui'`, () => {
    const fixture = TestBed.createComponent(CatalogComponent);
    const app = fixture.debugElement.componentInstance;
    expect(app.title).toEqual('ui');
  });

  it('should render title in a h1 tag', () => {
    const fixture = TestBed.createComponent(CatalogComponent);
    fixture.detectChanges();
    const compiled = fixture.debugElement.nativeElement;
    expect(compiled.querySelector('h1').textContent).toContain('Welcome to ui!');
  });
});
