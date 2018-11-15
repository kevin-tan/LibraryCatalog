import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { Routes, RouterModule } from '@angular/router';
import { RegistrationComponent } from "./registration/registration.component";
import { LoginComponent } from "./login/login.component";
import {CatalogComponent} from "./catalog/catalog.component";
import {HomeComponent} from "./home/home.component";
import {movieSearchComponent} from "./movieSearch/movieSearch.component";
import {musicSearchComponent} from "./musicSearch/musicSearch.component";
import {bookSearchComponent} from "./bookSearch/bookSearch.component";
import {magazineSearchComponent} from "./magazineSearch/magazineSearch.component";


const routes: Routes = [
  { path: '', redirectTo: '/catalog', pathMatch: 'full'},
  { path: 'register', component: RegistrationComponent},
  { path: 'login', component: LoginComponent},
  { path: 'catalog', component: CatalogComponent},
  { path: 'home', component: HomeComponent},
  { path:'search/magazine', component: magazineSearchComponent},
  { path:'search/movie', component: movieSearchComponent},
  { path:'search/music', component: musicSearchComponent},
  { path:'search/book', component: bookSearchComponent}
];

@NgModule({
  imports: [
    CommonModule,
    RouterModule.forRoot(routes)
  ],
  exports: [
    RouterModule
  ]
})
export class AppRoutingModule { }
export const routingComponents = [RegistrationComponent, LoginComponent, CatalogComponent]
