import { Routes } from '@angular/router';
import { Notfound } from './notfound';
import { Login } from './modules/login/login';

export const routes: Routes = [

  {
    path: 'admin',
    loadChildren: () => import('./modules/admin/admin.routes').then(m => m.ADMIN_ROUTES)
  },
  {
    path: 'login',
    component: Login
  },
  { path: 'notfound', component: Notfound },
  { path: '**', redirectTo: '/notfound' }
];
