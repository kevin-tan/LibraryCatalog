import {Component, OnInit, ViewChild} from '@angular/core';
import {HomeRedirectService} from './home-redirect.service';
import {HttpClient} from '@angular/common/http';
import {Book} from '../catalog/dto/book';
import {Magazine} from '../catalog/dto/magazine';
import {Movie} from '../catalog/dto/movie';
import {Music} from '../catalog/dto/music';
import {MatSort, MatTableDataSource} from '@angular/material';

@Component({
  selector: 'app-home',
  templateUrl: './home.component.html',
  styleUrls: ['./home.component.css']
})

export class HomeComponent implements OnInit {

  displayBookColumns: string[] = ['title', 'author', 'pages', 'format', 'publisher', 'isbn10', 'isbn13', 'pubDate', 'language'];
  matBookList: MatTableDataSource<Book>;
  bookList: Array<Book> = [];
  magazineList: Array<Magazine> = [];
  movieList: Array<Movie> = [];
  musicList: Array<Music> = [];

  @ViewChild(MatSort) sort: MatSort;

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
        this.matBookList = new MatTableDataSource(response['book'] as Array<Book>);
        this.matBookList.sort = this.sort;
        this.magazineList = response['magazine'] as Array<Magazine>;
        this.movieList = response['movie'] as Array<Movie>;
        this.musicList = response['music'] as Array<Music>;
      }, error => {
        console.log(error);
      });
  }
}
