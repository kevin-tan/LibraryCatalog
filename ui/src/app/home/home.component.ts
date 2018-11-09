import {Component, OnInit} from '@angular/core';
import {HomeRedirectService} from './home-redirect.service';
import {HttpClient} from '@angular/common/http';
import {Book} from '../catalog/dto/book';
import {Magazine} from '../catalog/dto/magazine';
import {Movie} from '../catalog/dto/movie';
import {Music} from '../catalog/dto/music';

@Component({
  selector: 'app-home',
  templateUrl: './home.component.html',
  styleUrls: ['./home.component.css']
})

export class HomeComponent implements OnInit {

  bookList: Array<Book> = [];
  magazineList: Array<Magazine> = [];
  movieList: Array<Movie> = [];
  musicList: Array<Music> = [];

  ngOnInit() {
    //Gets all itemSpecs at beginning
    this.getAllCatalog();
  }

  constructor(private http: HttpClient, private homeRedirectService: HomeRedirectService) {

  }

  logout(): void {
    let body = JSON.stringify({'email': sessionStorage.getItem('email')});
    this.http.post('http://localhost:8080/logout', body, {withCredentials:true}).subscribe(response => {
      this.homeRedirectService.redirect();
      sessionStorage.setItem('loggedIn', 'false');
      sessionStorage.setItem('email', '');
    }, error => {
      console.log(error);
    });
  }

  searchAllTitle(searchTitle: string) {
    this.http.get('http://localhost:8080/user/catalog/findByTitle/' + searchTitle,{withCredentials: true}).subscribe(response => {
      this.bookList = response['book'] as Array<Book>;
      this.magazineList = response['magazine'] as Array<Magazine>;
      this.movieList = response['movie'] as Array<Movie>;
      this.musicList = response['music'] as Array<Music>;
    }, error => {
      console.log(error);
    });
  }

  getAllCatalog() {
    this.http.get('http://localhost:8080/user/catalog/searchAll', {withCredentials: true}).subscribe(response => {
        this.bookList = response['book'] as Array<Book>;
        this.magazineList = response['magazine'] as Array<Magazine>;
        this.movieList = response['movie'] as Array<Movie>;
        this.musicList = response['music'] as Array<Music>;
      }, error => {
        console.log(error);
      });
  }
}
