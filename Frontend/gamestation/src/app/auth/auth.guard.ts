import { CanActivateFn, Router } from '@angular/router';

export const AuthGuard: CanActivateFn = (route, state) => {

  const isLoggedIn = !!localStorage.getItem('token'); 

  if (isLoggedIn) {
    return true;
  } else {
    window.location.href = '/login';
    return false;
  }
};