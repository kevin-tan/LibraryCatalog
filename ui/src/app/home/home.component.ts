import {Component, OnInit, ViewChild} from '@angular/core';
import {HttpClient} from '@angular/common/http';
import {Book} from '../catalog/dto/item-specification/book';
import {Magazine} from '../catalog/dto/item-specification/magazine';
import {Movie} from '../catalog/dto/item-specification/movie';
import {Music} from '../catalog/dto/item-specification/music';
import {MatSort, MatTableDataSource} from '@angular/material';
import {Router} from "@angular/router";
import {NgForm} from '@angular/forms';

@Component({
  selector: 'app-home',
  templateUrl: './home.component.html',
  styleUrls: ['./home.component.css']
})

export class HomeComponent implements OnInit {

  displayBookColumns: string[] = ['title', 'author', 'pages', 'format', 'publisher', 'isbn10', 'isbn13', 'pubDate', 'language', 'quantity'];
  bookList: Book[];
  matBookList: MatTableDataSource<Book>;

  displayMovieColumns: string[] = ['title', 'language', 'producers', 'actors', 'dubbed', 'subtitles', 'releaseDate' ,'runTime', 'quantity'];
  movieList: Movie[];
  matMovieList: MatTableDataSource<Movie>;

  displayMagazineColumns: string[] = ['title', 'publisher', 'pubDate', 'language', 'isbn10', 'isbn13', 'quantity'];
  magazineList: Magazine[];
  matMagazineList: MatTableDataSource<Magazine>;

  displayMusicColumns: string[] = ['title', 'artist', 'label', 'type', 'asin', 'releaseDate', 'quantity'];
  musicList: Music[];
  matMusicList: MatTableDataSource<Music>;

  @ViewChild('bookSort') bookSort: MatSort;
  @ViewChild('movieSort') movieSort: MatSort;
  @ViewChild('magazineSort') magazineSort: MatSort;
  @ViewChild('musicSort') musicSort: MatSort;
  @ViewChild('homeForm') homeForm: NgForm;
  route: any;

  ngOnInit() {
    //Gets all itemSpecs at beginning
    this.getAllCatalog();
  }

  constructor(private http: HttpClient, private router:Router) {

  }

  searchAllTitle(searchTitle: string) {
    this.http.get('http://localhost:8080/user/catalog/findByTitle/' + searchTitle, {withCredentials: true}).subscribe(response => {
      this.homeForm.resetForm();
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

  getAllCatalog() {
    this.http.get('http://localhost:8080/user/catalog/searchAll', {withCredentials: true}).subscribe(response => {
      this.homeForm.resetForm();
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

  OnSelectItem(itemType: string, itemSpecID: string){
    this.router.navigate(['/detail', itemType, itemSpecID])
  }
}
