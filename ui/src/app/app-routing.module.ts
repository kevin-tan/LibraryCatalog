import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { Routes, RouterModule } from '@angular/router';
import { RegistrationComponent } from "./registration/registration.component";
import { LoginComponent } from "./login/login.component";
import {CatalogComponent} from "./catalog/catalog.component";
import {LoginRedirectService} from "./login/login-redirect.service";
import {HomeComponent} from "./Home/home.component";


const routes: Routes = [
  { path: '', redirectTo: '/catalog', pathMatch: 'full'},
  { path: 'register', component: RegistrationComponent},
  { path: 'login', component: LoginComponent, canActivate: [LoginRedirectService]},
  { path: 'catalog', component: CatalogComponent},
  { path: 'home', component: HomeComponent}
];

@NgModule({
  imports: [
    CommonModule,
    RouterModule.forRoot(routes)
  ],
  exports: [
    RouterModule
  ],
  declarations: [
    HomeComponent
  ]
})
export class AppRoutingModule { }
export const routingComponents = [RegistrationComponent, LoginComponent, CatalogComponent]
