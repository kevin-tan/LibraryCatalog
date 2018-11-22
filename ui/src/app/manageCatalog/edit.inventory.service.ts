import {Injectable} from '@angular/core';
import {HttpClient, HttpHeaders} from '@angular/common/http';
import {SelectionModel} from '@angular/cdk/collections';
import {Book} from '../catalog/dto/item-specification/book';
import {NgForm} from '@angular/forms';
import {MatSnackBar, MatSort, MatTableDataSource} from '@angular/material';
import {Movie} from '../catalog/dto/item-specification/movie';
import {Magazine} from '../catalog/dto/item-specification/magazine';
import {Music} from '../catalog/dto/item-specification/music';

@Injectable()
export class EditInventoryService {

  headers: HttpHeaders = new HttpHeaders({'Content-Type': 'application/json'});
  options: Object = {headers: this.headers, withCredentials: true};

  constructor(private http: HttpClient) {
  }

  editBookInventory(bookSelection: SelectionModel<Book>, bookSelectedRow: Book, form: NgForm, bookList: Book[], bookSort: MatSort, snackBar: MatSnackBar, editType: string, quantDiff: number): Promise<MatTableDataSource<Book>> {
    if (bookSelection.isSelected(bookSelectedRow)) {
      let body = JSON.stringify({
        'Book': {
          'id': bookSelectedRow.id, 'title': bookSelectedRow.title, 'author': bookSelectedRow.author, 'publisher': bookSelectedRow.publisher, 'pubDate': bookSelectedRow.pubDate,
          'language': bookSelectedRow.language, 'format': bookSelectedRow.format, 'isbn10': bookSelectedRow.isbn10, 'isbn13': bookSelectedRow.isbn13, 'pages': bookSelectedRow.pages
        }
      });

      return this.http.post('http://localhost:8080/admin/catalog/' + sessionStorage.getItem('sessionId') + '/' + editType, body, this.options).toPromise().then(response => {
        for (let i = 0; i < bookList.length; i++) {
          if (bookList[i].id === bookSelectedRow.id) {
            bookList[i] = {
              'id': bookSelectedRow.id, 'title': bookSelectedRow.title, 'author': bookSelectedRow.author, 'publisher': bookSelectedRow.publisher, 'pubDate': bookSelectedRow.pubDate,
              'language': bookSelectedRow.language, 'format': bookSelectedRow.format, 'isbn10': bookSelectedRow.isbn10, 'isbn13': bookSelectedRow.isbn13, 'pages': +bookSelectedRow.pages, 'quantity': bookSelectedRow.quantity += quantDiff
            };
            break;
          }
        }
        snackBar.open('Item inventory changed successfully!', 'OK', {duration: 2000});

        let bookData = new MatTableDataSource(bookList);
        bookData.sort = bookSort;
        return bookData;
      });
    }
  }

  addBookItem(bookSelection: SelectionModel<Book>, bookSelectedRow: Book, form: NgForm, bookList: Book[], bookSort: MatSort, snackBar: MatSnackBar): Promise<MatTableDataSource<Book>> {
    return this.editBookInventory(bookSelection, bookSelectedRow, form, bookList, bookSort, snackBar, 'add', 1);
  }

  deleteBookItem(bookSelection: SelectionModel<Book>, bookSelectedRow: Book, form: NgForm, bookList: Book[], bookSort: MatSort, snackBar: MatSnackBar): Promise<MatTableDataSource<Book>> {
    if (bookSelectedRow.quantity <= 0) {
      bookSelection.clear();
      snackBar.open('No items to delete!', 'OK', {duration: 2000});
    } else {
      return this.editBookInventory(bookSelection, bookSelectedRow, form, bookList, bookSort, snackBar, 'delete', -1);
    }
  }

  editMovieInventory(movieSelection: SelectionModel<Movie>, movieSelectedRow: Movie, form: NgForm, movieList: Movie[], movieSort: MatSort, snackBar: MatSnackBar, editType: string, quantDiff: number): Promise<MatTableDataSource<Movie>> {
    if (movieSelection.isSelected(movieSelectedRow)) {
      let body = JSON.stringify({
        'Movie': {
          'id': movieSelectedRow.id, 'title': movieSelectedRow.title, 'director': movieSelectedRow.director, 'releaseDate': movieSelectedRow.releaseDate, 'language': movieSelectedRow.language,
          'subtitles': movieSelectedRow.subtitles, 'dubbed': movieSelectedRow.dubbed, 'actors': movieSelectedRow.actors, 'producers': movieSelectedRow.producers, 'runTime': movieSelectedRow.runTime
        }
      });

      return this.http.post('http://localhost:8080/admin/catalog/' + sessionStorage.getItem('sessionId') + '/' + editType, body, this.options).toPromise().then(response => {
        for (let i = 0; i < movieList.length; i++) {
          if (movieList[i].id === movieSelectedRow.id) {
            movieList[i] = {
              'id': movieSelectedRow.id, 'title': movieSelectedRow.title, 'director': movieSelectedRow.director, 'releaseDate': movieSelectedRow.releaseDate, 'language': movieSelectedRow.language,
              'subtitles': movieSelectedRow.subtitles, 'dubbed': movieSelectedRow.dubbed, 'actors': movieSelectedRow.actors, 'producers': movieSelectedRow.producers, 'runTime': movieSelectedRow.runTime, 'quantity': movieSelectedRow.quantity += quantDiff
            };
            break;
          }
        }
        snackBar.open('Item inventory changed successfully!', 'OK', {duration: 2000,});

        let movieData = new MatTableDataSource(movieList);
        movieData.sort = movieSort;
        return movieData;
      });
    }
  }

  addMovieItem(movieSelection: SelectionModel<Movie>, movieSelectedRow: Movie, form: NgForm, movieList: Movie[], movieSort: MatSort, snackBar: MatSnackBar): Promise<MatTableDataSource<Movie>>  {
    return this.editMovieInventory(movieSelection, movieSelectedRow, form, movieList, movieSort, snackBar, 'add', 1);
  }

  deleteMovieItem(movieSelection: SelectionModel<Movie>, movieSelectedRow: Movie, form: NgForm, movieList: Movie[], movieSort: MatSort, snackBar: MatSnackBar): Promise<MatTableDataSource<Movie>>  {
    if (movieSelectedRow.quantity <= 0) {
      movieSelection.clear();
      snackBar.open('No items to delete!', 'OK', {duration: 2000});
    } else {
      return this.editMovieInventory(movieSelection, movieSelectedRow, form, movieList, movieSort, snackBar, 'delete', -1);
    }
  }

  editMagazineInventory(magazineSelection: SelectionModel<Magazine>, magazineSelectedRow: Magazine, form: NgForm, magazineList: Magazine[], magazineSort: MatSort, snackBar: MatSnackBar, editType: string, quantDiff: number): Promise<MatTableDataSource<Magazine>> {
    if (magazineSelection.isSelected(magazineSelectedRow)) {
      let body = JSON.stringify({
        'Magazine': {
          'id': magazineSelectedRow.id, 'title': magazineSelectedRow.title, 'publisher': magazineSelectedRow.publisher, 'pubDate': magazineSelectedRow.pubDate,
          'language': magazineSelectedRow.language, 'isbn10': magazineSelectedRow.isbn10, 'isbn13': magazineSelectedRow.isbn13
        }
      });

      return this.http.post('http://localhost:8080/admin/catalog/' + sessionStorage.getItem('sessionId') + '/' + editType, body, this.options).toPromise().then(response => {
        for (let i = 0; i < magazineList.length; i++) {
          if (magazineList[i].id === magazineSelectedRow.id) {
            magazineList[i] = {
              'id': magazineSelectedRow.id, 'title': magazineSelectedRow.title, 'publisher': magazineSelectedRow.publisher, 'pubDate': magazineSelectedRow.pubDate,
              'language': magazineSelectedRow.language, 'isbn10': magazineSelectedRow.isbn10, 'isbn13': magazineSelectedRow.isbn13, 'quantity': magazineSelectedRow.quantity += quantDiff
            };
            break;
          }
        }
        snackBar.open('Item inventory changed successfully!', 'OK', {duration: 2000});

        let magazineData = new MatTableDataSource(magazineList);
        magazineData.sort = magazineSort;
        return magazineData;
      });
    }
  }

  addMagazineItem(magazineSelection: SelectionModel<Magazine>, magazineSelectedRow: Magazine, form: NgForm, magazineList: Magazine[], magazineSort: MatSort, snackBar: MatSnackBar): Promise<MatTableDataSource<Magazine>> {
    return this.editMagazineInventory(magazineSelection, magazineSelectedRow, form, magazineList, magazineSort, snackBar, 'add', 1);
  }

  deleteMagazineItem(magazineSelection: SelectionModel<Magazine>, magazineSelectedRow: Magazine, form: NgForm, magazineList: Magazine[], magazineSort: MatSort, snackBar: MatSnackBar): Promise<MatTableDataSource<Magazine>> {
    if (magazineSelectedRow.quantity <= 0) {
      magazineSelection.clear();
      snackBar.open('No items to delete!', 'OK', {duration: 2000});
    } else {
      return this.editMagazineInventory(magazineSelection, magazineSelectedRow, form, magazineList, magazineSort, snackBar, 'delete', -1);
    }
  }

  editMusicInventory(musicSelection: SelectionModel<Music>, musicSelectedRow: Music, form: NgForm, musicList: Music[], musicSort: MatSort, snackBar: MatSnackBar, editType: string, quantDiff: number): Promise<MatTableDataSource<Music>> {
    if (musicSelection.isSelected(musicSelectedRow)) {
      let body = JSON.stringify({
        'Music': {
          'id': musicSelectedRow.id, 'title': musicSelectedRow.title, 'artist': musicSelectedRow.artist, 'type': musicSelectedRow.type,
          'releaseDate': musicSelectedRow.releaseDate, 'label': musicSelectedRow.label, 'asin': musicSelectedRow.asin
        }
      });

      return this.http.post('http://localhost:8080/admin/catalog/' + sessionStorage.getItem('sessionId') + '/' + editType, body, this.options).toPromise().then(response => {
        for (let i = 0; i < musicList.length; i++) {
          if (musicList[i].id === musicSelectedRow.id) {
            musicList[i] = {
              'id': musicSelectedRow.id, 'title': musicSelectedRow.title, 'artist': musicSelectedRow.artist, 'type': musicSelectedRow.type,
              'releaseDate': musicSelectedRow.releaseDate, 'label': musicSelectedRow.label, 'asin': musicSelectedRow.asin, 'quantity': musicSelectedRow.quantity += quantDiff
            };
            break;
          }
        }
        snackBar.open('Item inventory changed successfully!', 'OK', {duration: 2000});

        let musicData = new MatTableDataSource(musicList);
        musicData.sort = musicSort;
        return musicData;
      });
    }
  }

  addMusicItem(musicSelection: SelectionModel<Music>, musicSelectedRow: Music, form: NgForm, musicList: Music[], musicSort: MatSort, snackBar: MatSnackBar): Promise<MatTableDataSource<Music>> {
    return this.editMusicInventory(musicSelection, musicSelectedRow, form, musicList, musicSort, snackBar, 'add', 1);
  }

  deleteMusicItem(musicSelection: SelectionModel<Music>, musicSelectedRow: Music, form: NgForm, musicList: Music[], musicSort: MatSort, snackBar: MatSnackBar): Promise<MatTableDataSource<Music>> {
    if (musicSelectedRow.quantity <= 0) {
      musicSelection.clear();
      snackBar.open('No items to delete!', 'OK', {duration: 2000});
    } else {
      return this.editMusicInventory(musicSelection, musicSelectedRow, form, musicList, musicSort, snackBar, 'delete', -1);
    }
  }
}
