import {Injectable} from '@angular/core';
import {HttpClient, HttpHeaders} from '@angular/common/http';
import {NgForm} from '@angular/forms';
import {MatSnackBar, MatTableDataSource, MatSort} from '@angular/material';
import {Book} from '../catalog/dto/item-specification/book';
import {Movie} from '../catalog/dto/item-specification/movie';
import {Magazine} from '../catalog/dto/item-specification/magazine';
import {Music} from '../catalog/dto/item-specification/music';

@Injectable()
export class AddItemSpecService {

  headers: HttpHeaders = new HttpHeaders({'Content-Type': 'application/json'});
  options: Object = {headers: this.headers, withCredentials: true};

  constructor(private http: HttpClient) {
  }

  addBookSpec(title: string, author: string, publisher: string, pubDate: string, language: string, format: string, isbn10: string,
              isbn13: string, pages: string, form: NgForm, bookList: Book[], snackBar: MatSnackBar, bookSort: MatSort): Promise<MatTableDataSource<Book>> {

    let body = JSON.stringify({
      'Book': {
        'title': title, 'author': author, 'publisher': publisher, 'pubDate': pubDate, 'language': language, 'format': format,
        'isbn10': isbn10, 'isbn13': isbn13, 'pages': +pages
      }
    });

    return this.http.post('http://localhost:8080/admin/catalog/' + sessionStorage.getItem('sessionId') + '/addSpec', body, this.options).toPromise().then(response => {
      form.resetForm();
      bookList.push({
        'id': null, 'title': title, 'author': author, 'publisher': publisher, 'pubDate': pubDate, 'language': language, 'format': format,
        'isbn10': isbn10, 'isbn13': isbn13, 'pages': +pages, 'quantity': 0
      });

      snackBar.open('Item added successfully!', 'OK', {duration: 2000});

      let bookData = new MatTableDataSource(bookList);
      bookData.sort = bookSort;
      return bookData;
    });
  }

  addMovieSpec(title: string, director: string, releaseDate: string, language: string, subtitles: string, dubbed: string, actors: string,
               producers: string, runTime: string, form: NgForm, movieList: Movie[], snackBar: MatSnackBar, movieSort: MatSort): Promise<MatTableDataSource<Movie>> {

    let body = JSON.stringify({
      'Movie': {
        'title': title, 'director': director, 'releaseDate': releaseDate, 'language': language, 'subtitles': subtitles, 'dubbed': dubbed.split(', '),
        'actors': actors.split(', '), 'producers': producers.split(', '), 'runTime': +runTime
      }
    });

    return this.http.post('http://localhost:8080/admin/catalog/' + sessionStorage.getItem('sessionId') + '/addSpec', body, this.options).toPromise().then(response => {
      form.resetForm();
      movieList.push({
        'id': null, 'title': title, 'director': director, 'releaseDate': releaseDate, 'language': language,
        'subtitles': subtitles, 'dubbed': dubbed.split(', '), 'actors': actors.split(', '),
        'producers': producers.split(', '), 'runTime': +runTime, 'quantity': 0
      });

      snackBar.open('Item added successfully!', 'OK', {duration: 2000});

      let movieData = new MatTableDataSource(movieList);
      movieData.sort = movieSort;
      return movieData;
    });
  }

  addMagazineSpec(title: string, publisher: string, pubDate: string, language: string, isbn10: string, isbn13: string, form: NgForm, magazineList: Magazine[], snackBar: MatSnackBar, magazineSort: MatSort): Promise<MatTableDataSource<Magazine>> {

    let body = JSON.stringify({
      'Magazine': {'title': title, 'publisher': publisher, 'pubDate': pubDate, 'language': language, 'isbn10': isbn10, 'isbn13': isbn13}
    });

    return this.http.post('http://localhost:8080/admin/catalog/' + sessionStorage.getItem('sessionId') + '/addSpec', body, this.options).toPromise().then(response => {
      form.resetForm();
      magazineList.push({
        'id': null, 'title': title, 'publisher': publisher, 'pubDate': pubDate, 'language': language,
        'isbn10': isbn10, 'isbn13': isbn13, 'quantity': 0
      });

      snackBar.open('Item added successfully!', 'OK', {duration: 2000});

      let magazineData = new MatTableDataSource(magazineList);
      magazineData.sort = magazineSort;
      return magazineData;
    });
  }

  addMusicSpec(title: string, artist: string, type: string, releaseDate: string, label: string, asin: string, form: NgForm, musicList: Music[], snackBar: MatSnackBar, musicSort: MatSort): Promise<MatTableDataSource<Music>> {
    let body = JSON.stringify({
      'Music': {'title': title, 'artist': artist, 'type': type, 'releaseDate': releaseDate, 'label': label, 'asin': asin}
    });

    return this.http.post('http://localhost:8080/admin/catalog/' + sessionStorage.getItem('sessionId') + '/addSpec', body, this.options).toPromise().then(response => {
      form.resetForm();
      musicList.push({
        'id': null, 'title': title, 'artist': artist, 'type': type, 'releaseDate': releaseDate, 'label': label,
        'asin': asin, 'quantity': 0
      });

      snackBar.open('Item added successfully!', 'OK', {duration: 2000});

      let musicData = new MatTableDataSource(musicList);
      musicData.sort = musicSort;
      return musicData;
    });
  }
}
