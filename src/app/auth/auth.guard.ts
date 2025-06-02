import { Injectable } from '@angular/core';
import { CanActivateFn, Router } from '@angular/router';

export const AuthGuard: CanActivateFn = (route, state) => {
  // Aquí deberías comprobar si el usuario está autenticado
  // Por ejemplo, usando localStorage o un AuthService
  const isLoggedIn = !!localStorage.getItem('token'); // O tu lógica de autenticación

  if (isLoggedIn) {
    return true;
  } else {
    // Redirige al login si no está autenticado
    window.location.href = '/login';
    return false;
  }
};