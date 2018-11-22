import {Component, OnInit, ViewChild} from '@angular/core';
import {HttpClient} from '@angular/common/http';
import {Book} from '../catalog/dto/item-specification/book';
import {Magazine} from '../catalog/dto/item-specification/magazine';
import {Movie} from '../catalog/dto/item-specification/movie';
import {Music} from '../catalog/dto/item-specification/music';
import {MatSnackBar, MatSort, MatTableDataSource} from '@angular/material';
import {NgForm} from '@angular/forms';
import {SelectionModel} from '@angular/cdk/collections';
import {AddItemSpecService} from './add.item.spec.service';
import {DeleteItemSpecService} from './delete.item.spec.service';
import {EditItemSpecService} from './edit.item.spec.service';
import {EditInventoryService} from './edit.inventory.service';

@Component({
  selector: 'manage-catalog',
  templateUrl: './manage.catalog.component.html',
  styleUrls: ['./manage.catalog.component.css']
})

export class ManageCatalogComponent implements OnInit {

  values = ['PAPERBACK', 'HARDCOVER'];
  selectedOption = this.values[0];

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
    this.getAllCatalog();
    this.getAllInventory();
  }

  constructor(private http: HttpClient, private addItemSpec: AddItemSpecService, private deleteItemSpec: DeleteItemSpecService,
              private editItemSpec: EditItemSpecService, private editInventory: EditInventoryService, public snackBar: MatSnackBar) {
  }

  startSession() {
    this.http.post<string>('http://localhost:8080/admin/catalog/edit', null, {withCredentials: true}).subscribe(response => {
      sessionStorage.setItem('sessionId', response['sessionId']);
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

  getAllInventory() {
    this.http.get('http://localhost:8080/user/catalog/getAll/itemSpec/quantity', {withCredentials: true}).subscribe(response => {
      for (let book of this.bookList) {
        book.quantity = response['Book'][book.id] >= 0 ? response['Book'][book.id] : 0;
      }
      for (let movie of this.movieList) {
        movie.quantity = response['Movie'][movie.id] >= 0 ? response['Movie'][movie.id] : 0;
      }
      for (let magazine of this.magazineList) {
        magazine.quantity = response['Magazine'][magazine.id] >= 0 ? response['Magazine'][magazine.id] : 0;
      }
      for (let music of this.musicList) {
        music.quantity = response['Music'][music.id] >= 0 ? response['Music'][music.id] : 0;
      }
    }, error => {
      console.log(error);
    });
  }

  saveAll() {
    this.http.post('http://localhost:8080/admin/catalog/' + sessionStorage.getItem('sessionId') + '/save', null, {withCredentials: true}).subscribe(response => {
      this.getAllCatalog();
      this.startSession();
      this.snackBar.open('Changes saved successfully', 'OK', {duration: 2000});
    });
  }

  //ADD SPEC
  addBook(title: string, author: string, publisher: string, pubDate: string, language: string, isbn10: string,
          isbn13: string, pages: string, form: NgForm) {
    this.addItemSpec.addBookSpec(title, author, publisher, pubDate, language, this.selectedOption, isbn10, isbn13, pages, form, this.bookList, this.snackBar, this.bookSort).then(value => {
      this.matBookList = value;
    });
  }

  addMovie(title: string, director: string, releaseDate: string, language: string, subtitles: string, dubbed: string, actors: string,
           producers: string, runTime: string, form: NgForm) {
    this.addItemSpec.addMovieSpec(title, director, releaseDate, language, subtitles, dubbed, actors, producers, runTime, form, this.movieList, this.snackBar, this.movieSort).then(value => {
      this.matMovieList = value;
    });
  }

  addMagazine(title: string, publisher: string, pubDate: string, language: string, isbn10: string, isbn13: string, form: NgForm) {
    this.addItemSpec.addMagazineSpec(title, publisher, pubDate, language, isbn10, isbn13, form, this.magazineList, this.snackBar, this.magazineSort).then(value => {
      this.matMagazineList = value;
    });
  }

  addMusic(title: string, artist: string, type: string, releaseDate: string, label: string, asin: string, form: NgForm) {
    this.addItemSpec.addMusicSpec(title, artist, type, releaseDate, label, asin, form, this.musicList, this.snackBar, this.musicSort).then(value => {
      this.matMusicList = value;
    });
  }

  //DELETE SPEC
  deleteBook() {
    this.deleteItemSpec.deleteBookSpec(this.bookSelection, this.bookSelectedRow, this.bookForm, this.bookList, this.bookSort, this.snackBar).then(value => {
      this.matBookList = value;
    }).catch(error => {
      this.bookSelection.clear();
      this.bookForm.resetForm();
      this.snackBar.open('An item of this type is out on loan!', 'OK', {duration: 2000});
    });
  }

  deleteMovie() {
    this.deleteItemSpec.deleteMovieSpec(this.movieSelection, this.movieSelectedRow, this.movieForm, this.movieList, this.movieSort, this.snackBar).then(value => {
      this.matMovieList = value;
    }).catch(error => {
      this.movieSelection.clear();
      this.movieForm.resetForm();
      this.snackBar.open('An item of this type is out on loan!', 'OK', {duration: 2000});
    });
  }

  deleteMagazine() {
    this.deleteItemSpec.deleteMagazineSpec(this.magazineSelection, this.magazineSelectedRow, this.magazineForm, this.magazineList, this.magazineSort, this.snackBar).then(value => {
      this.matMagazineList = value;
    }).catch(error => {
      this.magazineSelection.clear();
      this.magazineForm.resetForm();
      this.snackBar.open('An item of this type is out on loan!', 'OK', {duration: 2000});
    });
  }

  deleteMusic() {
    this.deleteItemSpec.deleteMusicSpec(this.musicSelection, this.musicSelectedRow, this.musicForm, this.musicList, this.musicSort, this.snackBar).then(value => {
      this.matMusicList = value;
    }).catch(error => {
      this.musicSelection.clear();
      this.musicForm.resetForm();
      this.snackBar.open('An item of this type is out on loan!', 'OK', {duration: 2000});
    });
  }

  //EDIT SPEC
  editBook(title: string, author: string, publisher: string, pubDate: string, language: string, isbn10: string,
           isbn13: string, pages: string, form: NgForm) {
    this.editItemSpec.editBookSpec(title, author, publisher, pubDate, language, this.selectedOption, isbn10, isbn13, pages, form, this.bookList, this.snackBar,
      this.bookSort, this.bookSelection, this.bookSelectedRow).then(value => {
      this.matBookList = value;
    });
  }

  editMovie(title: string, director: string, releaseDate: string, language: string, subtitles: string, dubbed: string, actors: string,
            producers: string, runTime: string, form: NgForm) {
    this.editItemSpec.editMovieSpec(title, director, releaseDate, language, subtitles, dubbed, actors, producers, runTime, form, this.movieList,
      this.snackBar, this.movieSort, this.movieSelection, this.movieSelectedRow).then(value => {
      this.matMovieList = value;
    });
  }

  editMagazine(title: string, publisher: string, pubDate: string, language: string, isbn10: string, isbn13: string, form: NgForm) {
    this.editItemSpec.editMagazineSpec(title, publisher, pubDate, language, isbn10, isbn13, form, this.magazineList, this.snackBar,
      this.magazineSort, this.magazineSelection, this.magazineSelectedRow).then(value => {
      this.matMagazineList = value;
    });
  }

  editMusic(title: string, artist: string, type: string, releaseDate: string, label: string, asin: string, form: NgForm) {
    this.editItemSpec.editMusicSpec(title, artist, type, releaseDate, label, asin, form, this.musicList, this.snackBar, this.musicSort,
      this.musicSelection, this.musicSelectedRow).then(value => {
      this.matMusicList = value;
    });
  }

  //ADD ITEM
  addBookItem() {
    this.editInventory.addBookItem(this.bookSelection, this.bookSelectedRow, this.bookForm, this.bookList, this.bookSort, this.snackBar).then(value => {
      this.matBookList = value;
    });
  }

  addMovieItem() {
    this.editInventory.addMovieItem(this.movieSelection, this.movieSelectedRow, this.movieForm, this.movieList, this.movieSort, this.snackBar).then(value => {
      this.matMovieList = value;
    });
  }

  addMagazineItem() {
    this.editInventory.addMagazineItem(this.magazineSelection, this.magazineSelectedRow, this.magazineForm, this.magazineList, this.magazineSort, this.snackBar).then(value => {
      this.matMagazineList = value;
    });
  }

  addMusicItem() {
    this.editInventory.addMusicItem(this.musicSelection, this.musicSelectedRow, this.musicForm, this.musicList, this.musicSort, this.snackBar).then(value => {
      this.matMusicList = value;
    });
  }

  //DELETE ITEM
  deleteBookItem() {
    this.editInventory.deleteBookItem(this.bookSelection, this.bookSelectedRow, this.bookForm, this.bookList, this.bookSort, this.snackBar).then(value => {
      this.matBookList = value;
    });
  }

  deleteMovieItem() {
    this.editInventory.deleteMovieItem(this.movieSelection, this.movieSelectedRow, this.movieForm, this.movieList, this.movieSort, this.snackBar).then(value => {
      this.matMovieList = value;
    });
  }

  deleteMagazineItem() {
    this.editInventory.deleteMagazineItem(this.magazineSelection, this.magazineSelectedRow, this.magazineForm, this.magazineList, this.magazineSort, this.snackBar).then(value => {
      this.matMagazineList = value;
    });
  }

  deleteMusicItem() {
    this.editInventory.deleteMusicItem(this.musicSelection, this.musicSelectedRow, this.musicForm, this.musicList, this.musicSort, this.snackBar).then(value => {
      this.matMusicList = value;
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
      this.selectedOption = row.format;
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
      (<HTMLInputElement>document.getElementById('mng_movie_producers')).value = row.producers.join(', ');
      (<HTMLInputElement>document.getElementById('mng_movie_actors')).value = row.actors.join(', ');
      (<HTMLInputElement>document.getElementById('mng_movie_dubbed')).value = row.dubbed.join(', ');
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
}
