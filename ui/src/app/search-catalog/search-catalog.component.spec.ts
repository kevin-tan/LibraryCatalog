import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { SearchCatalogComponent } from './search-catalog.component';

describe('SearchCatalogComponent', () => {
  let component: SearchCatalogComponent;
  let fixture: ComponentFixture<SearchCatalogComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ SearchCatalogComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(SearchCatalogComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
