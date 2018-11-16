import { BrowserModule } from '@angular/platform-browser';
import { NgModule } from '@angular/core';
import { HttpClientModule } from '@angular/common/http';
import { FormsModule} from "@angular/forms";
import { AppComponent } from './app.component';
import { AppRoutingModule, routingComponents } from './app-routing.module';
import { LoginComponent } from './login/login.component';
import { CatalogComponent } from './catalog/catalog.component';
import { HomeComponent } from './home/home.component';
import {LoginRedirectService} from "./login/login-redirect.service";
import {bookSearchComponent} from "./bookSearch/bookSearch.component";
import {musicSearchComponent} from "./musicSearch/musicSearch.component";
import {movieSearchComponent} from "./movieSearch/movieSearch.component";
import {magazineSearchComponent} from "./magazineSearch/magazineSearch.component";
import {activeUsersComponent} from "./activeUsers/activeUsers.component";
import {HomeRedirectService} from "./home/home-redirect.service";

@NgModule({
  declarations: [
    AppComponent,
    routingComponents,
    LoginComponent,
    CatalogComponent,
    HomeComponent,
    bookSearchComponent,
    musicSearchComponent,
    movieSearchComponent,
    magazineSearchComponent,
    activeUsersComponent
  ],
  imports: [
    BrowserModule,
    FormsModule,
    HttpClientModule,
    AppRoutingModule
  ],
  providers: [LoginRedirectService, HomeRedirectService],
  bootstrap: [AppComponent]
})
export class AppModule { }
