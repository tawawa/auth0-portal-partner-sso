import { Routes } from '@angular/router';
import { IndexComponent } from './index/index.component';
import { UserComponent } from './user/user.component';
import { AuthComponent } from './auth/auth.component';
import { CallbackComponent } from './callback/callback.component';

export const ROUTES: Routes = [
  { path: '', component: IndexComponent },
  { path: 'user', component: UserComponent },
  { path: 'auth', component: AuthComponent },
  { path: 'callback', component: CallbackComponent },
  { path: '**', redirectTo: '' }
];
