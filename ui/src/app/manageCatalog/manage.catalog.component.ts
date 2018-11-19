import {Component, OnInit, ViewChild} from '@angular/core';
import {HttpClient, HttpHeaders} from '@angular/common/http';
import {Book} from '../catalog/dto/item-specification/book';
import {Magazine} from '../catalog/dto/item-specification/magazine';
import {Movie} from '../catalog/dto/item-specification/movie';
import {Music} from '../catalog/dto/item-specification/music';
import {MatSnackBar, MatSort, MatTableDataSource} from '@angular/material';
import {HomeRedirectService} from '../home/home-redirect.service';
import {NgForm} from '@angular/forms';
import {SelectionModel} from '@angular/cdk/collections';

@Component({
  selector: 'manage-catalog',
  templateUrl: './manage.catalog.component.html',
  styleUrls: ['./manage.catalog.component.css']
})

export class ManageCatalogComponent implements OnInit {

  displayBookColumns: string[] = ['select', 'title', 'author', 'pages', 'format', 'publisher', 'isbn10', 'isbn13', 'pubDate', 'language', 'quantity'];
  bookList: Book[];
  matBookList: MatTableDataSource<Book>;
  bookSelection = new SelectionModel<Book>(false, []);
  bookSelectedRow: Book;

  displayMovieColumns: string[] = ['select', 'title', 'language', 'director', 'producers', 'actors', 'dubbed', 'subtitles', 'releaseDate', 'runTime', 'quantity'];
  movieList: Movie[];
  matMovieList: MatTableDataSource<Movie>;
  movieSelection = new SelectionModel<Movie>(false, []);
  movieSelectedRow: Movie;

  displayMagazineColumns: string[] = ['select', 'title', 'publisher', 'pubDate', 'language', 'isbn10', 'isbn13', 'quantity'];
  magazineList: Magazine[];
  matMagazineList: MatTableDataSource<Magazine>;
  magazineSelection = new SelectionModel<Magazine>(false, []);
  magazineSelectedRow: Magazine;

  displayMusicColumns: string[] = ['select', 'title', 'artist', 'label', 'type', 'asin', 'releaseDate', 'quantity'];
  musicList: Music[];
  matMusicList: MatTableDataSource<Music>;
  musicSelection = new SelectionModel<Music>(false, []);
  musicSelectedRow: Music;

  @ViewChild('bookSort') bookSort: MatSort;
  @ViewChild('movieSort') movieSort: MatSort;
  @ViewChild('magazineSort') magazineSort: MatSort;
  @ViewChild('musicSort') musicSort: MatSort;

  @ViewChild('bookForm') bookForm: NgForm;
  @ViewChild('movieForm') movieForm: NgForm;
  @ViewChild('magazineForm') magazineForm: NgForm;
  @ViewChild('musicForm') musicForm: NgForm;

  ngOnInit() {
    this.startSession();
    //Gets all itemSpecs at beginning
    this.getAllCatalog();
    this.getAllInventory();
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

  getAllCatalog() {
    this.http.get('http://localhost:8080/user/catalog/searchAll', {withCredentials: true}).subscribe(response => {
      this.matBookList = new MatTableDataSource(response['book'] as Array<Book>);
      this.bookList = response['book'] as Array<Book>;
      this.matBookList.sort = this.bookSort;

      this.matMagazineList = new MatTableDataSource(response['magazine'] as Array<Magazine>);
      this.magazineList = response['magazine'] as Array<Magazine>;
      this.matMagazineList.sort = this.magazineSort;

      this.matMovieList = new MatTableDataSource(response['movie'] as Array<Movie>);
      this.movieList = response['movie'] as Array<Movie>;
      this.matMovieList.sort = this.movieSort;

      this.matMusicList = new MatTableDataSource(response['music'] as Array<Music>);
      this.musicList = response['music'] as Array<Music>;
      this.matMusicList.sort = this.musicSort;

      this.getAllInventory();
    }, error => {
      console.log(error);
    });
  }

  startSession() {
    this.http.post<string>('http://localhost:8080/admin/catalog/edit', null, {withCredentials: true}).subscribe(response => {
      sessionStorage.setItem('sessionId', response['sessionId']);
    });
  }

  addBook(title: string, author: string, publisher: string, pubDate: string, language: string, format: string, isbn10: string,
          isbn13: string, pages: string, form: NgForm) {

    let body = JSON.stringify({
      'Book': {
        'title': title,
        'author': author,
        'publisher': publisher,
        'pubDate': pubDate,
        'language': language,
        'format': format,
        'isbn10': isbn10,
        'isbn13': isbn13,
        'pages': +pages
      }
    });

    let headers = new HttpHeaders({'Content-Type': 'application/json'});
    let options = {headers: headers, withCredentials: true};

    this.http.post('http://localhost:8080/admin/catalog/' + sessionStorage.getItem('sessionId') + '/addSpec', body, options).subscribe(response => {
      form.resetForm();
      this.bookList.push({'id': null, 'title': title, 'author': author, 'publisher': publisher, 'pubDate': pubDate, 'language': language,
        'format': format, 'isbn10': isbn10, 'isbn13': isbn13, 'pages': +pages, 'quantity': 0});
      this.matBookList = new MatTableDataSource(this.bookList);
      this.matBookList.sort = this.bookSort;
      this.snackBar.open('Item added successfully!', 'OK', {
        duration: 2000,
      });
    }, error => {
      console.log(error);
    });
  }

  addMovie(title: string, director: string, releaseDate: string, language: string, subtitles: string, dubbed: string, actors: string,
           producers: string, runTime: string, form: NgForm) {

    let body = JSON.stringify({
      'Movie': {
        'title': title,
        'director': director,
        'releaseDate': releaseDate,
        'language': language,
        'subtitles': subtitles,
        'dubbed': dubbed.split(', '),
        'actors': actors.split(', '),
        'producers': producers.split(', '),
        'runTime': +runTime
      }
    });

    let headers = new HttpHeaders({'Content-Type': 'application/json'});
    let options = {headers: headers, withCredentials: true};

    this.http.post('http://localhost:8080/admin/catalog/' + sessionStorage.getItem('sessionId') + '/addSpec', body, options).subscribe(response => {
      form.resetForm();
      this.movieList.push({'id': null, 'title': title, 'director': director, 'releaseDate': releaseDate, 'language': language,
        'subtitles': subtitles, 'dubbed': dubbed.split(', '), 'actors': actors.split(', '),
        'producers': producers.split(', '), 'runTime': +runTime, 'quantity': 0});
      this.matMovieList = new MatTableDataSource(this.movieList);
      this.matMovieList.sort = this.movieSort;
    }, error => {
      console.log(error);
    });
  }

  addMagazine(title: string, publisher: string, pubDate: string, language: string, isbn10: string, isbn13: string, form: NgForm) {
    let body = JSON.stringify({
      'Magazine': {
        'title': title,
        'publisher': publisher,
        'pubDate': pubDate,
        'language': language,
        'isbn10': isbn10,
        'isbn13': isbn13
      }
    });

    let headers = new HttpHeaders({'Content-Type': 'application/json'});
    let options = {headers: headers, withCredentials: true};

    this.http.post('http://localhost:8080/admin/catalog/' + sessionStorage.getItem('sessionId') + '/addSpec', body, options).subscribe(response => {
      form.resetForm();
      this.magazineList.push({'id': null, 'title': title, 'publisher': publisher, 'pubDate': pubDate, 'language': language,
        'isbn10': isbn10, 'isbn13': isbn13,'quantity': 0});
      this.matMagazineList = new MatTableDataSource(this.magazineList);
      this.matMagazineList.sort = this.magazineSort;
    }, error => {
      console.log(error);
    });
  }

  addMusic(title: string, artist: string, type: string, releaseDate: string, label: string, asin: string, form: NgForm) {
    let body = JSON.stringify({
      'Music': {
        'title': title,
        'artist': artist,
        'type': type,
        'releaseDate': releaseDate,
        'label': label,
        'asin': asin
      }
    });

    let headers = new HttpHeaders({'Content-Type': 'application/json'});
    let options = {headers: headers, withCredentials: true};

    this.http.post('http://localhost:8080/admin/catalog/' + sessionStorage.getItem('sessionId') + '/addSpec', body, options).subscribe(response => {
      form.resetForm();
      this.musicList.push({'id': null, 'title': title, 'artist': artist, 'type': type, 'releaseDate': releaseDate, 'label': label, 'asin': asin, 'quantity': 0});
      this.matMusicList = new MatTableDataSource(this.musicList);
      this.matMusicList.sort = this.musicSort;
    }, error => {
      console.log(error);
    });
  }

  saveAll() {
    this.http.post('http://localhost:8080/admin/catalog/' + sessionStorage.getItem('sessionId') + '/save', null, {withCredentials: true}).subscribe(response => {
      this.getAllCatalog();
      this.startSession();
      this.snackBar.open('Changes saved successfully', 'OK', {
        duration: 2000,
      });
    });
  }

  bookRowSelected(row: Book) {
    this.bookSelectedRow = row;

    if (!this.bookSelection.isSelected(row)) {
      (<HTMLInputElement>document.getElementById('mng_book_title')).value = row.title;
      (<HTMLInputElement>document.getElementById('mng_book_author')).value = row.author;
      (<HTMLInputElement>document.getElementById('mng_book_publisher')).value = row.publisher;
      (<HTMLInputElement>document.getElementById('mng_book_pubDate')).value = row.pubDate;
      (<HTMLInputElement>document.getElementById('mng_book_language')).value = row.language;
      (<HTMLInputElement>document.getElementById('mng_book_format')).value = row.format;
      (<HTMLInputElement>document.getElementById('mng_book_isbn10')).value = row.isbn10;
      (<HTMLInputElement>document.getElementById('mng_book_isbn13')).value = row.isbn13;
      (<HTMLInputElement>document.getElementById('mng_book_pages')).value = row.pages.toString();
    } else {
      this.bookForm.resetForm();
    }
  }

  movieRowSelected(row: Movie) {
    this.movieSelectedRow = row;

    if (!this.movieSelection.isSelected(row)) {
      (<HTMLInputElement>document.getElementById('mng_movie_title')).value = row.title;
      (<HTMLInputElement>document.getElementById('mng_movie_language')).value = row.language;
      (<HTMLInputElement>document.getElementById('mng_movie_director')).value = row.director;
      (<HTMLInputElement>document.getElementById('mng_movie_producers')).value = row.producers.join(", ");
      (<HTMLInputElement>document.getElementById('mng_movie_actors')).value = row.actors.join(", ");
      (<HTMLInputElement>document.getElementById('mng_movie_dubbed')).value = row.dubbed.join(", ");
      (<HTMLInputElement>document.getElementById('mng_movie_subtitles')).value = row.subtitles;
      (<HTMLInputElement>document.getElementById('mng_movie_releaseDate')).value = row.releaseDate;
      (<HTMLInputElement>document.getElementById('mng_movie_runtime')).value = row.runTime.toString();
    } else {
      this.movieForm.resetForm();
    }
  }

  magazineRowSelected(row: Magazine) {
    this.magazineSelectedRow = row;

    if (!this.magazineSelection.isSelected(row)) {
      (<HTMLInputElement>document.getElementById('mng_magazine_title')).value = row.title;
      (<HTMLInputElement>document.getElementById('mng_magazine_language')).value = row.language;
      (<HTMLInputElement>document.getElementById('mng_magazine_publisher')).value = row.publisher;
      (<HTMLInputElement>document.getElementById('mng_magazine_pubDate')).value = row.pubDate;
      (<HTMLInputElement>document.getElementById('mng_magazine_isbn10')).value = row.isbn10;
      (<HTMLInputElement>document.getElementById('mng_magazine_isbn13')).value = row.isbn13;
    } else {
      this.magazineForm.resetForm();
    }
  }

  musicRowSelected(row: Music) {
    this.musicSelectedRow = row;

    if (!this.musicSelection.isSelected(row)) {
      (<HTMLInputElement>document.getElementById('mng_music_title')).value = row.title;
      (<HTMLInputElement>document.getElementById('mng_music_artist')).value = row.artist;
      (<HTMLInputElement>document.getElementById('mng_music_releaseDate')).value = row.releaseDate;
      (<HTMLInputElement>document.getElementById('mng_music_type')).value = row.type;
      (<HTMLInputElement>document.getElementById('mng_music_label')).value = row.label;
      (<HTMLInputElement>document.getElementById('mng_music_asin')).value = row.asin;
    } else {
      this.musicForm.resetForm();
    }
  }

  editBook(title: string, author: string, publisher: string, pubDate: string, language: string, format: string, isbn10: string,
           isbn13: string, pages: string, form: NgForm) {
    if (this.bookSelection.isSelected(this.bookSelectedRow) && this.bookSelectedRow.id !== null) {
      let body = JSON.stringify({
        'Book': {
          'id': this.bookSelectedRow.id,
          'title': title,
          'author': author,
          'publisher': publisher,
          'pubDate': pubDate,
          'language': language,
          'format': format,
          'isbn10': isbn10,
          'isbn13': isbn13,
          'pages': pages
        }
      });

      let headers = new HttpHeaders({'Content-Type': 'application/json'});
      let options = {headers: headers, withCredentials: true};

      this.http.post('http://localhost:8080/admin/catalog/' + sessionStorage.getItem('sessionId') + '/modifySpec', body, options).subscribe(response => {
        form.resetForm();

        for (let i = 0; i < this.bookList.length; i++) {
          if (this.bookList[i].id === this.bookSelectedRow.id) {
            this.bookList[i] = {'id': this.bookSelectedRow.id, 'title': title, 'author': author, 'publisher': publisher, 'pubDate': pubDate,
              'language': language, 'format': format, 'isbn10': isbn10, 'isbn13': isbn13, 'pages': +pages, 'quantity': 0};
            break;
          }
        }
        this.matBookList = new MatTableDataSource(this.bookList);
        this.matBookList.sort = this.bookSort;
        this.snackBar.open('Item modified successfully!', 'OK', {
          duration: 2000,
        });
      }, error => {
        console.log(error);
      });
    } else {
      this.bookSelection.clear();
      this.snackBar.open('Please select a valid row first', 'OK', {
        duration: 2000,
      });
    }
  }

  getAllInventory() {
    this.http.get('http://localhost:8080/user/catalog/getAll/itemSpec/quantity', {withCredentials: true}).subscribe(response => {
      for( let book of this.bookList){
        book.quantity = response['Book'][book.id] >= 0 ? response['Book'][book.id] : 0;
      }
      for( let movie of this.movieList){
        movie.quantity = response['Movie'][movie.id] >= 0 ? response['Movie'][movie.id] : 0;
      }
      for( let magazine of this.magazineList){
        magazine.quantity = response['Magazine'][magazine.id] >= 0 ? response['Magazine'][magazine.id] : 0;
      }
      for( let music of this.musicList){
        music.quantity = response['Music'][music.id] >= 0 ? response['Music'][music.id] : 0;
      }
    }, error => {
      console.log(error);
    });
  }

  addBookItem() {
    if (this.bookSelection.isSelected(this.bookSelectedRow) && this.bookSelectedRow.id !== null) {
      let body = JSON.stringify({
        'Book': {
          'id': this.bookSelectedRow.id,
          'title': this.bookSelectedRow.title,
          'author': this.bookSelectedRow.author,
          'publisher': this.bookSelectedRow.publisher,
          'pubDate': this.bookSelectedRow.pubDate,
          'language': this.bookSelectedRow.language,
          'format': this.bookSelectedRow.format,
          'isbn10': this.bookSelectedRow.isbn10,
          'isbn13': this.bookSelectedRow.isbn13,
          'pages': this.bookSelectedRow.pages
        }
      });

      let headers = new HttpHeaders({'Content-Type': 'application/json'});
      let options = {headers: headers, withCredentials: true};

      this.http.post('http://localhost:8080/admin/catalog/' + sessionStorage.getItem('sessionId') + '/add', body, options).subscribe(response => {
        for (let i = 0; i < this.bookList.length; i++) {
          if (this.bookList[i].id === this.bookSelectedRow.id) {
            this.bookList[i] = {'id': this.bookSelectedRow.id, 'title': this.bookSelectedRow.title, 'author': this.bookSelectedRow.author,
              'publisher': this.bookSelectedRow.publisher, 'pubDate': this.bookSelectedRow.pubDate, 'language': this.bookSelectedRow.language,
              'format': this.bookSelectedRow.format, 'isbn10': this.bookSelectedRow.isbn10, 'isbn13': this.bookSelectedRow.isbn13,
              'pages': +this.bookSelectedRow.pages, 'quantity': ++this.bookSelectedRow.quantity};
            break;
          }
        }
        this.matBookList = new MatTableDataSource(this.bookList);
        this.matBookList.sort = this.bookSort;
        this.snackBar.open('Item inventory changed successfully!', 'OK', {
          duration: 2000,
        });
      }, error => {
        console.log(error);
      });
    } else {
      this.bookSelection.clear();
      this.snackBar.open('Please select a valid row first', 'OK', {
        duration: 2000,
      });
    }
  }

  deleteBookItem() {
    if (this.bookSelection.isSelected(this.bookSelectedRow) && this.bookSelectedRow.id !== null) {
      let body = JSON.stringify({
        'Book': {
          'id': this.bookSelectedRow.id,
          'title': this.bookSelectedRow.title,
          'author': this.bookSelectedRow.author,
          'publisher': this.bookSelectedRow.publisher,
          'pubDate': this.bookSelectedRow.pubDate,
          'language': this.bookSelectedRow.language,
          'format': this.bookSelectedRow.format,
          'isbn10': this.bookSelectedRow.isbn10,
          'isbn13': this.bookSelectedRow.isbn13,
          'pages': this.bookSelectedRow.pages
        }
      });

      let headers = new HttpHeaders({'Content-Type': 'application/json'});
      let options = {headers: headers, withCredentials: true};

      this.http.post('http://localhost:8080/admin/catalog/' + sessionStorage.getItem('sessionId') + '/delete', body, options).subscribe(response => {
        for (let i = 0; i < this.bookList.length; i++) {
          if (this.bookList[i].id === this.bookSelectedRow.id) {
            this.bookList[i] = {'id': this.bookSelectedRow.id, 'title': this.bookSelectedRow.title, 'author': this.bookSelectedRow.author,
              'publisher': this.bookSelectedRow.publisher, 'pubDate': this.bookSelectedRow.pubDate, 'language': this.bookSelectedRow.language,
              'format': this.bookSelectedRow.format, 'isbn10': this.bookSelectedRow.isbn10, 'isbn13': this.bookSelectedRow.isbn13,
              'pages': +this.bookSelectedRow.pages, 'quantity': --this.bookSelectedRow.quantity};
            break;
          }
        }
        this.matBookList = new MatTableDataSource(this.bookList);
        this.matBookList.sort = this.bookSort;
        this.snackBar.open('Item inventory changed successfully!', 'OK', {
          duration: 2000,
        });
      }, error => {
        console.log(error);
      });
    } else {
      this.bookSelection.clear();
      this.snackBar.open('Please select a valid row first', 'OK', {
        duration: 2000,
      });
    }
  }

  addMovieItem() {
    if (this.movieSelection.isSelected(this.movieSelectedRow) && this.movieSelectedRow.id !== null) {
      let body = JSON.stringify({
        'Movie': {
          'id': this.movieSelectedRow.id,
          'title': this.movieSelectedRow.title,
          'director': this.movieSelectedRow.director,
          'releaseDate': this.movieSelectedRow.releaseDate,
          'language': this.movieSelectedRow.language,
          'subtitles': this.movieSelectedRow.subtitles,
          'dubbed': this.movieSelectedRow.dubbed,
          'actors': this.movieSelectedRow.actors,
          'producers': this.movieSelectedRow.producers,
          'runTime': this.movieSelectedRow.runTime
        }
      });

      let headers = new HttpHeaders({'Content-Type': 'application/json'});
      let options = {headers: headers, withCredentials: true};

      this.http.post('http://localhost:8080/admin/catalog/' + sessionStorage.getItem('sessionId') + '/add', body, options).subscribe(response => {
        for (let i = 0; i < this.movieList.length; i++) {
          if (this.movieList[i].id === this.movieSelectedRow.id) {
            this.movieList[i] = {'id': this.movieSelectedRow.id, 'title': this.movieSelectedRow.title, 'director': this.movieSelectedRow.director,
              'releaseDate': this.movieSelectedRow.releaseDate, 'language': this.movieSelectedRow.language, 'subtitles': this.movieSelectedRow.subtitles,
              'dubbed': this.movieSelectedRow.dubbed, 'actors': this.movieSelectedRow.actors, 'producers': this.movieSelectedRow.producers,
              'runTime': this.movieSelectedRow.runTime, 'quantity': ++this.movieSelectedRow.quantity};
            break;
          }
        }
        this.matMovieList = new MatTableDataSource(this.movieList);
        this.matMovieList.sort = this.movieSort;
        this.snackBar.open('Item inventory changed successfully!', 'OK', {
          duration: 2000,
        });
      }, error => {
        console.log(error);
      });
    } else {
      this.movieSelection.clear();
      this.snackBar.open('Please select a valid row first', 'OK', {
        duration: 2000,
      });
    }
  }

  addMagazineItem() {
    if (this.magazineSelection.isSelected(this.magazineSelectedRow) && this.magazineSelectedRow.id !== null) {
      let body = JSON.stringify({
        'Magazine': {
          'id': this.magazineSelectedRow.id,
          'title': this.magazineSelectedRow.title,
          'publisher': this.magazineSelectedRow.publisher,
          'pubDate': this.magazineSelectedRow.pubDate,
          'language': this.magazineSelectedRow.language,
          'isbn10': this.magazineSelectedRow.isbn10,
          'isbn13': this.magazineSelectedRow.isbn13
        }
      });

      let headers = new HttpHeaders({'Content-Type': 'application/json'});
      let options = {headers: headers, withCredentials: true};

      this.http.post('http://localhost:8080/admin/catalog/' + sessionStorage.getItem('sessionId') + '/add', body, options).subscribe(response => {
        for (let i = 0; i < this.magazineList.length; i++) {
          if (this.magazineList[i].id === this.magazineSelectedRow.id) {
            this.magazineList[i] = {'id': this.magazineSelectedRow.id, 'title': this.magazineSelectedRow.title, 'publisher': this.magazineSelectedRow.publisher,
              'pubDate': this.magazineSelectedRow.pubDate, 'language': this.magazineSelectedRow.language, 'isbn10': this.magazineSelectedRow.isbn10,
              'isbn13': this.magazineSelectedRow.isbn13, 'quantity': ++this.magazineSelectedRow.quantity};
            break;
          }
        }
        this.matMagazineList = new MatTableDataSource(this.magazineList);
        this.matMagazineList.sort = this.magazineSort;
        this.snackBar.open('Item inventory changed successfully!', 'OK', {
          duration: 2000,
        });
      }, error => {
        console.log(error);
      });
    } else {
      this.magazineSelection.clear();
      this.snackBar.open('Please select a valid row first', 'OK', {
        duration: 2000,
      });
    }
  }

  addMusicItem() {
    if (this.musicSelection.isSelected(this.musicSelectedRow) && this.musicSelectedRow.id !== null) {
      let body = JSON.stringify({
        'Music': {
          'id': this.musicSelectedRow.id,
          'title': this.musicSelectedRow.title,
          'artist': this.musicSelectedRow.artist,
          'type': this.musicSelectedRow.type,
          'releaseDate': this.musicSelectedRow.releaseDate,
          'label': this.musicSelectedRow.label,
          'asin': this.musicSelectedRow.asin
        }
      });

      let headers = new HttpHeaders({'Content-Type': 'application/json'});
      let options = {headers: headers, withCredentials: true};

      this.http.post('http://localhost:8080/admin/catalog/' + sessionStorage.getItem('sessionId') + '/add', body, options).subscribe(response => {
        for (let i = 0; i < this.musicList.length; i++) {
          if (this.musicList[i].id === this.musicSelectedRow.id) {
            this.musicList[i] = {'id': this.musicSelectedRow.id, 'title': this.musicSelectedRow.title, 'artist': this.musicSelectedRow.artist,
              'type': this.musicSelectedRow.type, 'releaseDate': this.musicSelectedRow.releaseDate, 'label': this.musicSelectedRow.label,
              'asin': this.musicSelectedRow.asin, 'quantity': ++this.musicSelectedRow.quantity};
            break;
          }
        }
        this.matMusicList = new MatTableDataSource(this.musicList);
        this.matMusicList.sort = this.musicSort;
        this.snackBar.open('Item inventory changed successfully!', 'OK', {
          duration: 2000,
        });
      }, error => {
        console.log(error);
      });
    } else {
      this.musicSelection.clear();
      this.snackBar.open('Please select a valid row first', 'OK', {
        duration: 2000,
      });
    }
  }
}
