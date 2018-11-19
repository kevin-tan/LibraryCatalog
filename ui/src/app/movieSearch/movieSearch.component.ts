import {Component, OnInit, ViewChild} from '@angular/core';
import {Movie} from "../catalog/dto/item-specification/movie";
import {HttpClient, HttpHeaders} from "@angular/common/http";
import {MatSort, MatTableDataSource} from '@angular/material';
import {Router} from "@angular/router";
import {NgForm} from '@angular/forms';

@Component({
  selector: 'app-catalog',
  templateUrl: './movieSearch.component.html',
  styleUrls: ['./movieSearch.component.css']
})
export class movieSearchComponent implements OnInit {

  displayMovieColumns: string[] = ['title', 'language', 'director', 'producers', 'actors', 'dubbed', 'subtitles', 'releaseDate' ,'runTime', 'quantity'];
  movieList: Movie[];
  matMovieList: MatTableDataSource<Movie>;

  @ViewChild('movieSort') movieSort: MatSort;
  @ViewChild('movieForm') movieForm: NgForm;

  constructor(private http: HttpClient, private router: Router) { }

  ngOnInit() {
    this.getAllMovies();
  }

  getAllMovies(): void {
    this.http.get<Array<Movie>>('http://localhost:8080/user/catalog/getAll/movie', {withCredentials: true}).subscribe(response => {
      this.movieForm.resetForm();
      this.matMovieList = new MatTableDataSource(response);
      this.movieList = response;
      this.matMovieList.sort = this.movieSort;

      this.getAllInventory();
    }, error => {
      console.log(error);
    });
  }
  searchMovies(title: string,
               director: string,
               releaseDate: string,
               language: string,
               subtitles: string,
               runTime: string) {

    let body = JSON.stringify( {
        "title": title,
        "director": director,
        "releaseDate": releaseDate,
        "language": language,
        "subtitles": subtitles,
        "runTime": runTime === "" ? "" : +runTime
    });

    let headers = new HttpHeaders({"Content-Type": "application/json"});
    let options = {headers: headers, withCredentials: true};
    this.http.post<Array<Movie>>('http://localhost:8080/user/catalog/search/movie', body, options).subscribe(response => {
      this.movieForm.resetForm();
      this.matMovieList = new MatTableDataSource(response);
      this.movieList = response;
      this.matMovieList.sort = this.movieSort;

      this.getAllInventory();
    }, error => {
      console.log(error);
    });
  }

  getAllInventory() {
    this.http.get('http://localhost:8080/user/catalog/getAll/itemSpec/quantity', {withCredentials: true}).subscribe(response => {
      for (let movie of this.movieList) {
        movie.quantity = response['Movie'][movie.id] >= 0 ? response['Movie'][movie.id] : 0;
      }
    }, error => {
      console.log(error);
    });
  }

  OnSelectItem(itemType: string, itemSpecID: string){
    this.router.navigate(['/detail', itemType, itemSpecID])
  }
}
