import { ComponentFixture, TestBed } from '@angular/core/testing';

import { SchedaImportComponent } from './scheda-import.component';

describe('SchedaImportComponent', () => {
  let component: SchedaImportComponent;
  let fixture: ComponentFixture<SchedaImportComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [SchedaImportComponent],
    }).compileComponents();

    fixture = TestBed.createComponent(SchedaImportComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
