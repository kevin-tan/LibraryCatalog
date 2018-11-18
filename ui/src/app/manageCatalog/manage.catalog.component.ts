import {Component, OnInit, ViewChild} from '@angular/core';
import {HttpClient, HttpHeaders} from '@angular/common/http';
import {Book} from '../catalog/dto/book';
import {Magazine} from '../catalog/dto/magazine';
import {Movie} from '../catalog/dto/movie';
import {Music} from '../catalog/dto/music';
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

  selectedRow: Book;

  displayBookColumns: string[] = ['select', 'title', 'author', 'pages', 'format', 'publisher', 'isbn10', 'isbn13', 'pubDate', 'language'];
  bookList: Book[];
  matBookList: MatTableDataSource<Book>;
  bookSelection = new SelectionModel<Book>(false, []);

  displayMovieColumns: string[] = ['title', 'language', 'producers', 'actors', 'dubbed', 'subtitles', 'releaseDate', 'runTime'];
  movieList: Movie[];
  matMovieList: MatTableDataSource<Movie>;

  displayMagazineColumns: string[] = ['title', 'publisher', 'pubDate', 'language', 'isbn10', 'isbn13'];
  magazineList: Magazine[];
  matMagazineList: MatTableDataSource<Magazine>;

  displayMusicColumns: string[] = ['title', 'artist', 'label', 'type', 'asin', 'releaseDate'];
  musicList: Music[];
  matMusicList: MatTableDataSource<Music>;

  @ViewChild('bookSort') bookSort: MatSort;
  @ViewChild('movieSort') movieSort: MatSort;
  @ViewChild('magazineSort') magazineSort: MatSort;
  @ViewChild('musicSort') musicSort: MatSort;

  @ViewChild('bookForm') bookForm: NgForm;

  ngOnInit() {
    this.startSession();
    //Gets all itemSpecs at beginning
    this.getAllCatalog();
  }

  constructor(private http: HttpClient, private homeRedirectService: HomeRedirectService, public snackBar: MatSnackBar) {

  }

  /** Whether the number of selected elements matches the total number of rows. */
  isAllSelected() {
    const numSelected = this.bookSelection.selected.length;
    const numRows = this.matBookList.data.length;
    return numSelected === numRows;
  }

  /** Selects all rows if they are not all selected; otherwise clear selection. */
  masterToggle() {
    this.isAllSelected() ?
      this.bookSelection.clear() :
      this.matBookList.data.forEach(row => this.bookSelection.select(row));
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
      this.magazineList = response['magazine'] as Array<Magazine>;
      this.matMagazineList.sort = this.magazineSort;

      this.matMovieList = new MatTableDataSource(response['movie'] as Array<Movie>);
      this.movieList = response['movie'] as Array<Movie>;
      this.matMovieList.sort = this.movieSort;

      this.matMusicList = new MatTableDataSource(response['music'] as Array<Music>);
      this.musicList = response['music'] as Array<Music>;
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

  addBook(title: string,
              author: string,
              publisher: string,
              pubDate: string,
              language: string,
              format: string,
              isbn10: string,
              isbn13: string,
              pages: string,
              form: NgForm) {

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
        "pages": +pages
      }
    });

    let headers = new HttpHeaders({"Content-Type": "application/json"});
    let options = {headers: headers, withCredentials: true};

    this.http.post('http://localhost:8080/admin/catalog/' + sessionStorage.getItem('sessionId') + '/addSpec', body, options).subscribe(response => {
      form.resetForm();
      this.bookList.push({
        "id": null,
        "title": title,
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
      this.snackBar.open('Item added successfully!', 'OK', {
        duration: 2000,
      });
    }, error => {
      console.log(error);
    });
  }

  addMovie(title: string,
               director: string,
               releaseDate: string,
               language: string,
               subtitles: string,
               dubbed: string,
               actors: string,
               producers: string,
               runTime: string,
               form: NgForm) {

    let body = JSON.stringify( {
      "Movie": {
        "title": title,
        "director": director,
        "releaseDate": releaseDate,
        "language": language,
        "subtitles": subtitles,
        "dubbed": dubbed.split(", "),
        "actors": actors.split(", "),
        "producers": producers.split(", "),
        "runTime": +runTime
      }
    });

    let headers = new HttpHeaders({"Content-Type": "application/json"});
    let options = {headers: headers, withCredentials: true};

    this.http.post('http://localhost:8080/admin/catalog/' + sessionStorage.getItem('sessionId') + '/addSpec', body, options).subscribe(response => {
      form.resetForm();
      this.movieList.push({
        "id": null,
        "title": title,
        "director": director,
        "releaseDate": releaseDate,
        "language": language,
        "subtitles": subtitles,
        "dubbed": dubbed.split(", "),
        "actors": actors.split(", "),
        "producers": producers.split(", "),
        "runTime": +runTime});
      this.matMovieList = new MatTableDataSource(this.movieList);
      this.matMovieList.sort = this.movieSort;
    }, error => {
      console.log(error);
    });
  }

  addMagazine(title: string,
                  publisher: string,
                  pubDate: string,
                  language: string,
                  isbn10: string,
                  isbn13: string,
                  form: NgForm) {

    let body = JSON.stringify( {
      "Magazine": {
        "title": title,
        "publisher": publisher,
        "pubDate": pubDate,
        "language": language,
        "isbn10": isbn10,
        "isbn13": isbn13
      }
    });

    let headers = new HttpHeaders({"Content-Type": "application/json"});
    let options = {headers: headers, withCredentials: true};

    this.http.post('http://localhost:8080/admin/catalog/' + sessionStorage.getItem('sessionId') + '/addSpec', body, options).subscribe(response => {
      form.resetForm();
      this.magazineList.push({
        "id": null,
        "title": title,
        "publisher": publisher,
        "pubDate": pubDate,
        "language": language,
        "isbn10": isbn10,
        "isbn13": isbn13});
      this.matMagazineList = new MatTableDataSource(this.magazineList);
      this.matMagazineList.sort = this.magazineSort;
    }, error => {
      console.log(error);
    });
  }

  addMusic(title: string,
               artist: string,
               type: string,
               releaseDate: string,
               label: string,
               asin: string,
               form: NgForm) {

    let body = JSON.stringify( {
      "Music": {
        "title": title,
        "artist": artist,
        "type": type,
        "releaseDate": releaseDate,
        "label": label,
        "asin": asin
      }
    });

    let headers = new HttpHeaders({"Content-Type": "application/json"});
    let options = {headers: headers, withCredentials: true};

    this.http.post('http://localhost:8080/admin/catalog/' + sessionStorage.getItem('sessionId') + '/addSpec', body, options).subscribe(response => {
      form.resetForm();
      this.musicList.push({
        "id": null,
        "title": title,
        "artist": artist,
        "type": type,
        "releaseDate": releaseDate,
        "label": label,
        "asin": asin});
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

  rowSelected(row: Book) {
    this.selectedRow = row;

    if(!this.bookSelection.isSelected(row)) {
      (<HTMLInputElement>document.getElementById("mng_book_title")).value = row.title;
      (<HTMLInputElement>document.getElementById("mng_book_author")).value = row.author;
      (<HTMLInputElement>document.getElementById("mng_book_publisher")).value = row.publisher;
      (<HTMLInputElement>document.getElementById("mng_book_pubDate")).value = row.pubDate;
      (<HTMLInputElement>document.getElementById("mng_book_language")).value = row.language;
      (<HTMLInputElement>document.getElementById("mng_book_format")).value = row.format;
      (<HTMLInputElement>document.getElementById("mng_book_isbn10")).value = row.isbn10;
      (<HTMLInputElement>document.getElementById("mng_book_isbn13")).value = row.isbn13;
      (<HTMLInputElement>document.getElementById("mng_book_pages")).value = row.pages.toString();
    } else {
      this.bookForm.resetForm();
    }
  }

  editBook(title: string,
           author: string,
           publisher: string,
           pubDate: string,
           language: string,
           format: string,
           isbn10: string,
           isbn13: string,
           pages: string,
           form: NgForm) {
    if(this.bookSelection.isSelected(this.selectedRow) && this.selectedRow.id !== null) {
      let body = JSON.stringify({
        "Book": {
          "id": this.selectedRow.id,
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

      this.http.post('http://localhost:8080/admin/catalog/' + sessionStorage.getItem('sessionId') + '/modifySpec', body, options).subscribe(response => {
        form.resetForm();

        for (let i = 0; i < this.bookList.length; i++) {
          if (this.bookList[i].id === this.selectedRow.id) {
            this.bookList[i] = {
              "id": this.selectedRow.id,
              "title": title,
              "author": author,
              "publisher": publisher,
              "pubDate": pubDate,
              "language": language,
              "format": format,
              "isbn10": isbn10,
              "isbn13": isbn13,
              "pages": +pages
            };
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
}