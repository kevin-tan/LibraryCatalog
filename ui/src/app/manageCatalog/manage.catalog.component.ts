import {Component, OnInit, ViewChild} from '@angular/core';
import {HttpClient, HttpHeaders} from '@angular/common/http';
import {Book} from '../catalog/dto/book';
import {Magazine} from '../catalog/dto/magazine';
import {Movie} from '../catalog/dto/movie';
import {Music} from '../catalog/dto/music';
import {MatSnackBar, MatSort, MatTableDataSource} from '@angular/material';
import {HomeRedirectService} from '../home/home-redirect.service';
import {NgForm} from '@angular/forms';

@Component({
  selector: 'manage-catalog',
  templateUrl: './manage.catalog.component.html',
  styleUrls: ['./manage.catalog.component.css']
})

export class ManageCatalogComponent implements OnInit {

  displayBookColumns: string[] = ['title', 'author', 'pages', 'format', 'publisher', 'isbn10', 'isbn13', 'pubDate', 'language'];
  bookList: Book[];
  matBookList: MatTableDataSource<Book>;

  displayMovieColumns: string[] = ['title', 'language', 'producers', 'actors', 'dubbed', 'subtitles', 'releaseDate', 'runTime'];
  matMovieList: MatTableDataSource<Movie>;

  displayMagazineColumns: string[] = ['title', 'publisher', 'pubDate', 'language', 'isbn10', 'isbn13'];
  matMagazineList: MatTableDataSource<Magazine>;

  displayMusicColumns: string[] = ['title', 'artist', 'label', 'type', 'asin', 'releaseDate'];
  matMusicList: MatTableDataSource<Music>;

  @ViewChild('bookSort') bookSort: MatSort;
  @ViewChild('movieSort') movieSort: MatSort;
  @ViewChild('magazineSort') magazineSort: MatSort;
  @ViewChild('musicSort') musicSort: MatSort;

  ngOnInit() {
    this.startSession();
    //Gets all itemSpecs at beginning
    this.getAllCatalog();
  }

  constructor(private http: HttpClient, private homeRedirectService: HomeRedirectService, public snackBar: MatSnackBar) {

  }

  logout() {
    let body = JSON.stringify({'email': sessionStorage.getItem('email')});
    this.http.post('http://localhost:8080/logout', body, {withCredentials: true}).subscribe(response => {
      this.homeRedirectService.redirect();
      sessionStorage.setItem('loggedIn', 'false');
      sessionStorage.setItem('email', '');
    }, error => {
      console.log(error);
    });
  }

  searchAllTitle(searchTitle: string) {
    this.http.get('http://localhost:8080/user/catalog/findByTitle/' + searchTitle, {withCredentials: true}).subscribe(response => {
      this.matBookList = new MatTableDataSource(response['book'] as Array<Book>);
      this.matBookList.sort = this.bookSort;
      this.matMagazineList = new MatTableDataSource(response['magazine'] as Array<Magazine>);
      this.matMagazineList.sort = this.magazineSort;
      this.matMovieList = new MatTableDataSource(response['movie'] as Array<Movie>);
      this.matMovieList.sort = this.movieSort;
      this.matMusicList = new MatTableDataSource(response['music'] as Array<Music>);
      this.matMusicList.sort = this.musicSort;
    }, error => {
      console.log(error);
    });
  }

  getAllCatalog() {
    this.http.get('http://localhost:8080/user/catalog/searchAll', {withCredentials: true}).subscribe(response => {
      this.matBookList = new MatTableDataSource(response['book'] as Array<Book>);
      this.bookList = response['book'] as Array<Book>;
      this.matBookList.sort = this.bookSort;
      this.matMagazineList = new MatTableDataSource(response['magazine'] as Array<Magazine>);
      this.matMagazineList.sort = this.magazineSort;
      this.matMovieList = new MatTableDataSource(response['movie'] as Array<Movie>);
      this.matMovieList.sort = this.movieSort;
      this.matMusicList = new MatTableDataSource(response['music'] as Array<Music>);
      this.matMusicList.sort = this.musicSort;
    }, error => {
      console.log(error);
    });
  }

  startSession() {
    this.http.post<string>('http://localhost:8080/admin/catalog/edit', null, {withCredentials: true}).subscribe(response => {
      sessionStorage.setItem('sessionId', response['sessionId']);
    });
  }

  //TODO Note that the method is always called even on invalid input, so verify before proceeding

  searchBooks(title: string,
              author: string,
              publisher: string,
              pubDate: string,
              language: string,
              format: string,
              isbn10: string,
              isbn13: string,
              pages: string,
              form: NgForm) {

    console.log(form.pristine);

    let body = JSON.stringify( {
      "Book": {
        "title": title,
        "author": author,
        "publisher": publisher,
        "pubDate": pubDate,
        "language": language,
        "format": format,
        "isbn10": isbn10,
        "isbn13": isbn13,
        "pages": pages
      }
    });

    let headers = new HttpHeaders({"Content-Type": "application/json"});
    let options = {headers: headers, withCredentials: true};

    this.http.post('http://localhost:8080/admin/catalog/' + sessionStorage.getItem('sessionId') + '/addSpec', body, options).subscribe(response => {
      form.resetForm();
      this.bookList.push({"title": title,
        "author": author,
        "publisher": publisher,
        "pubDate": pubDate,
        "language": language,
        "format": format,
        "isbn10": isbn10,
        "isbn13": isbn13,
        "pages": +pages});
      this.matBookList = new MatTableDataSource(this.bookList);
      this.matBookList.sort = this.bookSort;
    }, error => {
      console.log(error);
    });
  }

  searchMovies(title: string,
               director: string,
               releaseDate: string,
               language: string,
               subtitles: string,
               dubbed: string,
               actors: string,
               producers: string) {

    console.log('Title: ' + title);
  }

  searchMagazines(title: string,
                  publisher: string,
                  pubDate: string,
                  language: string,
                  isbn10: string,
                  isbn13: string) {

    console.log('Title: ' + title);
  }

  searchMusics(title: string,
               artist: string,
               type: string,
               releaseDate: string,
               label: string,
               asin: string) {

    console.log('Title: ' + title);
  }

  saveAll() {
    this.http.post('http://localhost:8080/admin/catalog/' + sessionStorage.getItem('sessionId') + '/save', null, {withCredentials: true}).subscribe(response => {
      this.snackBar.open('Changes saved successfully', 'OK', {
        duration: 2000,
      });
    });
  }
}
