import { ComponentFixture, TestBed } from '@angular/core/testing';

import { VideojuegoPerfilComponent } from './videojuego-perfil.component';

describe('VideojuegoPerfilComponent', () => {
  let component: VideojuegoPerfilComponent;
  let fixture: ComponentFixture<VideojuegoPerfilComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [VideojuegoPerfilComponent]
    })
    .compileComponents();

    fixture = TestBed.createComponent(VideojuegoPerfilComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
