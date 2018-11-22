import {Injectable} from '@angular/core';
import {HttpClient, HttpHeaders} from '@angular/common/http';
import {MatSnackBar, MatSort, MatTableDataSource} from '@angular/material';
import {SelectionModel} from '@angular/cdk/collections';
import {Book} from '../catalog/dto/item-specification/book';
import {NgForm} from '@angular/forms';
import {Movie} from '../catalog/dto/item-specification/movie';
import {Magazine} from '../catalog/dto/item-specification/magazine';
import {Music} from '../catalog/dto/item-specification/music';

@Injectable()
export class DeleteItemSpecService {

  headers: HttpHeaders = new HttpHeaders({'Content-Type': 'application/json'});
  options: Object = {headers: this.headers, withCredentials: true};

  constructor(private http: HttpClient) {
  }

  deleteBookSpec(bookSelection: SelectionModel<Book>, bookSelectedRow: Book, form: NgForm, bookList: Book[], bookSort: MatSort, snackBar: MatSnackBar): Promise<MatTableDataSource<Book>> {
    if (bookSelection.isSelected(bookSelectedRow)) {
      let body = JSON.stringify({
        'Book': {
          'id': bookSelectedRow.id, 'title': bookSelectedRow.title, 'author': bookSelectedRow.author, 'publisher': bookSelectedRow.publisher, 'pubDate': bookSelectedRow.pubDate,
          'language': bookSelectedRow.language, 'format': bookSelectedRow.format, 'isbn10': bookSelectedRow.isbn10, 'isbn13': bookSelectedRow.isbn13, 'pages': bookSelectedRow.pages
        }
      });

      return this.http.post('http://localhost:8080/admin/catalog/' + sessionStorage.getItem('sessionId') + '/deleteSpec', body, this.options).toPromise().then(response => {
        form.resetForm();
        for (let i = 0; i < bookList.length; i++) {
          if (bookList[i].id === bookSelectedRow.id) {
            bookList.splice(i, 1);
            break;
          }
        }
        snackBar.open('Item deleted successfully!', 'OK', {duration: 2000});

        let bookData = new MatTableDataSource(bookList);
        bookData.sort = bookSort;
        return bookData;
      });
    } else {
      bookSelection.clear();
      snackBar.open('Please select a row first', 'OK', {duration: 2000});
    }
  }

  deleteMovieSpec(movieSelection: SelectionModel<Movie>, movieSelectedRow: Movie, form: NgForm, movieList: Movie[], movieSort: MatSort, snackBar: MatSnackBar): Promise<MatTableDataSource<Movie>> {
    if (movieSelection.isSelected(movieSelectedRow)) {
      let body = JSON.stringify({
        'Movie': {
          'id': movieSelectedRow.id, 'title': movieSelectedRow.title, 'director': movieSelectedRow.director, 'releaseDate': movieSelectedRow.releaseDate, 'language': movieSelectedRow.language,
          'subtitles': movieSelectedRow.subtitles, 'dubbed': movieSelectedRow.dubbed, 'actors': movieSelectedRow.actors, 'producers': movieSelectedRow.producers, 'runTime': movieSelectedRow.runTime
        }
      });

      return this.http.post('http://localhost:8080/admin/catalog/' + sessionStorage.getItem('sessionId') + '/deleteSpec', body, this.options).toPromise().then(response => {
        form.resetForm();
        for (let i = 0; i < movieList.length; i++) {
          if (movieList[i].id === movieSelectedRow.id) {
            movieList.splice(i, 1);
            break;
          }
        }
        snackBar.open('Item deleted successfully!', 'OK', {duration: 2000});

        let movieData = new MatTableDataSource(movieList);
        movieData.sort = movieSort;
        return movieData;
      });
    } else {
      movieSelection.clear();
      snackBar.open('Please select a valid row first', 'OK', {duration: 2000});
    }
  }

  deleteMagazineSpec(magazineSelection: SelectionModel<Magazine>, magazineSelectedRow: Magazine, form: NgForm, magazineList: Magazine[], magazineSort: MatSort, snackBar: MatSnackBar): Promise<MatTableDataSource<Magazine>> {
    if (magazineSelection.isSelected(magazineSelectedRow)) {
      let body = JSON.stringify({
        'Magazine': {
          'id': magazineSelectedRow.id, 'title': magazineSelectedRow.title, 'publisher': magazineSelectedRow.publisher, 'pubDate': magazineSelectedRow.pubDate,
          'language': magazineSelectedRow.language, 'isbn10': magazineSelectedRow.isbn10, 'isbn13': magazineSelectedRow.isbn13
        }
      });

      return this.http.post('http://localhost:8080/admin/catalog/' + sessionStorage.getItem('sessionId') + '/deleteSpec', body, this.options).toPromise().then(response => {
        form.resetForm();
        for (let i = 0; i < magazineList.length; i++) {
          if (magazineList[i].id === magazineSelectedRow.id) {
            magazineList.splice(i, 1);
            break;
          }
        }
        snackBar.open('Item deleted successfully!', 'OK', {duration: 2000});

        let magazineData = new MatTableDataSource(magazineList);
        magazineData.sort = magazineSort;
        return magazineData;
      });
    } else {
      magazineSelection.clear();
      snackBar.open('Please select a valid row first', 'OK', {duration: 2000});
    }
  }

  deleteMusicSpec(musicSelection: SelectionModel<Music>, musicSelectedRow: Music, form: NgForm, musicList: Music[], musicSort: MatSort, snackBar: MatSnackBar): Promise<MatTableDataSource<Music>> {
    if (musicSelection.isSelected(musicSelectedRow)) {
      let body = JSON.stringify({
        'Music': {
          'id': musicSelectedRow.id, 'title': musicSelectedRow.title, 'artist': musicSelectedRow.artist, 'type': musicSelectedRow.type,
          'releaseDate': musicSelectedRow.releaseDate, 'label': musicSelectedRow.label, 'asin': musicSelectedRow.asin
        }
      });

      return this.http.post('http://localhost:8080/admin/catalog/' + sessionStorage.getItem('sessionId') + '/deleteSpec', body, this.options).toPromise().then(response => {
        form.resetForm();
        for (let i = 0; i < musicList.length; i++) {
          if (musicList[i].id === musicSelectedRow.id) {
            musicList.splice(i, 1);
            break;
          }
        }
        snackBar.open('Item deleted successfully!', 'OK', {duration: 2000});

        let musicData = new MatTableDataSource(musicList);
        musicData.sort = musicSort;
        return musicData;
      });
    } else {
      musicSelection.clear();
      snackBar.open('Please select a valid row first', 'OK', {duration: 2000,});
    }
  }
}
