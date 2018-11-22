import {Injectable} from '@angular/core';
import {HttpClient, HttpHeaders} from '@angular/common/http';
import {NgForm} from '@angular/forms';
import {Book} from '../catalog/dto/item-specification/book';
import {MatSnackBar, MatSort, MatTableDataSource} from '@angular/material';
import {SelectionModel} from '@angular/cdk/collections';
import {Movie} from '../catalog/dto/item-specification/movie';
import {Magazine} from '../catalog/dto/item-specification/magazine';
import {Music} from '../catalog/dto/item-specification/music';

@Injectable()
export class EditItemSpecService {

  headers: HttpHeaders = new HttpHeaders({'Content-Type': 'application/json'});
  options: Object = {headers: this.headers, withCredentials: true};

  constructor(private http: HttpClient) {
  }

  editBookSpec(title: string, author: string, publisher: string, pubDate: string, language: string, format: string, isbn10: string, isbn13: string,
               pages: string, form: NgForm, bookList: Book[], snackBar: MatSnackBar, bookSort: MatSort, bookSelection: SelectionModel<Book>, bookSelectedRow: Book): Promise<MatTableDataSource<Book>> {
    if (bookSelection.isSelected(bookSelectedRow)) {
      let body = JSON.stringify({
        'Book': {
          'id': bookSelectedRow.id, 'title': title, 'author': author, 'publisher': publisher, 'pubDate': pubDate, 'language': language,
          'format': format, 'isbn10': isbn10, 'isbn13': isbn13, 'pages': pages
        }
      });

      return this.http.post('http://localhost:8080/admin/catalog/' + sessionStorage.getItem('sessionId') + '/modifySpec', body, this.options).toPromise().then(response => {
        form.resetForm();
        for (let i = 0; i < bookList.length; i++) {
          if (bookList[i].id === bookSelectedRow.id) {
            bookList[i] = {
              'id': bookSelectedRow.id, 'title': title, 'author': author, 'publisher': publisher, 'pubDate': pubDate, 'language': language,
              'format': format, 'isbn10': isbn10, 'isbn13': isbn13, 'pages': +pages, 'quantity': bookSelectedRow.quantity
            };
            break;
          }
        }
        snackBar.open('Item modified successfully!', 'OK', {duration: 2000});

        let bookData = new MatTableDataSource(bookList);
        bookData.sort = bookSort;
        return bookData;
      });
    }
  }

  editMovieSpec(title: string, director: string, releaseDate: string, language: string, subtitles: string, dubbed: string, actors: string, producers: string,
                runTime: string, form: NgForm, movieList: Movie[], snackBar: MatSnackBar, movieSort: MatSort, movieSelection: SelectionModel<Movie>, movieSelectedRow: Movie): Promise<MatTableDataSource<Movie>> {
    if (movieSelection.isSelected(movieSelectedRow)) {
      let body = JSON.stringify({
        'Movie': {
          'id': movieSelectedRow.id, 'title': title, 'director': director, 'releaseDate': releaseDate, 'language': language, 'subtitles': subtitles,
          'dubbed': dubbed.split(', '), 'actors': actors.split(', '), 'producers': producers.split(', '), 'runTime': +runTime
        }
      });

      return this.http.post('http://localhost:8080/admin/catalog/' + sessionStorage.getItem('sessionId') + '/modifySpec', body, this.options).toPromise().then(response => {
        form.resetForm();
        for (let i = 0; i < movieList.length; i++) {
          if (movieList[i].id === movieSelectedRow.id) {
            movieList[i] = {
              'id': movieSelectedRow.id, 'title': title, 'director': director, 'releaseDate': releaseDate, 'language': language,
              'subtitles': subtitles, 'dubbed': dubbed.split(', '), 'actors': actors.split(', '), 'producers': producers.split(', '),
              'runTime': +runTime, 'quantity': movieSelectedRow.quantity
            };
            break;
          }
        }
        snackBar.open('Item modified successfully!', 'OK', {duration: 2000});

        let movieData = new MatTableDataSource(movieList);
        movieData.sort = movieSort;
        return movieData;
      });
    }
  }

  editMagazineSpec(title: string, publisher: string, pubDate: string, language: string, isbn10: string, isbn13: string, form: NgForm, magazineList: Magazine[],
                   snackBar: MatSnackBar, magazineSort: MatSort, magazineSelection: SelectionModel<Magazine>, magazineSelectedRow: Magazine): Promise<MatTableDataSource<Magazine>> {
    if (magazineSelection.isSelected(magazineSelectedRow)) {
      let body = JSON.stringify({
        'Magazine': {'id': magazineSelectedRow.id, 'title': title, 'publisher': publisher, 'pubDate': pubDate, 'language': language, 'isbn10': isbn10, 'isbn13': isbn13}
      });

      return this.http.post('http://localhost:8080/admin/catalog/' + sessionStorage.getItem('sessionId') + '/modifySpec', body, this.options).toPromise().then(response => {
        form.resetForm();
        for (let i = 0; i < magazineList.length; i++) {
          if (magazineList[i].id === magazineSelectedRow.id) {
            magazineList[i] = {
              'id': magazineSelectedRow.id, 'title': title, 'publisher': publisher, 'pubDate': pubDate, 'language': language,
              'isbn10': isbn10, 'isbn13': isbn13, 'quantity': magazineSelectedRow.quantity
            };
            break;
          }
        }
        snackBar.open('Item modified successfully!', 'OK', {duration: 2000});

        let magazineData = new MatTableDataSource(magazineList);
        magazineData.sort = magazineSort;
        return magazineData;
      });
    }
  }

  editMusicSpec(title: string, artist: string, type: string, releaseDate: string, label: string, asin: string, form: NgForm, musicList: Music[],
                snackBar: MatSnackBar, musicSort: MatSort, musicSelection: SelectionModel<Music>, musicSelectedRow: Music): Promise<MatTableDataSource<Music>> {
    if (musicSelection.isSelected(musicSelectedRow)) {
      let body = JSON.stringify({
        'Music': {'id': musicSelectedRow.id, 'title': title, 'artist': artist, 'type': type, 'releaseDate': releaseDate, 'label': label, 'asin': asin}
      });

      return this.http.post('http://localhost:8080/admin/catalog/' + sessionStorage.getItem('sessionId') + '/modifySpec', body, this.options).toPromise().then(response => {
        form.resetForm();
        for (let i = 0; i < musicList.length; i++) {
          if (musicList[i].id === musicSelectedRow.id) {
            musicList[i] = {
              'id': musicSelectedRow.id, 'title': title, 'artist': artist, 'type': type, 'releaseDate': releaseDate, 'label': label,
              'asin': asin, 'quantity': musicSelectedRow.quantity
            };
            break;
          }
        }
        snackBar.open('Item modified successfully!', 'OK', {duration: 2000});

        let musicData = new MatTableDataSource(musicList);
        musicData.sort = musicSort;
        return musicData;
      });
    }
  }
}
