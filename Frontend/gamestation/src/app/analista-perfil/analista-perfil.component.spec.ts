import { ComponentFixture, TestBed } from '@angular/core/testing';

import { AnalistaComponent } from './analista-perfil.component';

describe('AnalistaPerfilComponent', () => {
  let component: AnalistaComponent;
  let fixture: ComponentFixture<AnalistaComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [AnalistaComponent]
    })
    .compileComponents();

    fixture = TestBed.createComponent(AnalistaComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
