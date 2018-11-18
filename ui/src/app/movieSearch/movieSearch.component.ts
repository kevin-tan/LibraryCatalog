import {Component, OnInit, ViewChild} from '@angular/core';
import {Movie} from "../catalog/dto/item-specification/movie";
import {HttpClient, HttpHeaders} from "@angular/common/http";
import {MatSort, MatTableDataSource} from '@angular/material';
import {Router} from "@angular/router";

@Component({
  selector: 'app-catalog',
  templateUrl: './movieSearch.component.html',
  styleUrls: ['./movieSearch.component.css']
})
export class movieSearchComponent implements OnInit {

  displayMovieColumns: string[] = ['title', 'language', 'producers', 'actors', 'dubbed', 'subtitles', 'releaseDate' ,'runTime'];
  matMovieList: MatTableDataSource<Movie>;

  @ViewChild('movieSort') movieSort: MatSort;

  constructor(private http: HttpClient, private router: Router) { }

  ngOnInit() {
    this.getAllMovies();
  }

  getAllMovies(): void {
    this.http.get<Array<Movie>>('http://localhost:8080/user/catalog/getAll/movie', {withCredentials: true}).subscribe(response => {
      this.matMovieList = new MatTableDataSource(response);
      this.matMovieList.sort = this.movieSort;
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

    let body = JSON.stringify({
      "title": title,
      "director": director,
      "releaseDate": releaseDate,
      "language": language,
      "actors": actors,
      "subtitles": subtitles,
      "dubbed": dubbed,
      "producers": producers
    });

    let headers = new HttpHeaders({"Content-Type": "application/json"});
    let options = {headers: headers, withCredentials: true};
    this.http.post<Array<Movie>>('http://localhost:8080/user/catalog/search/movie', body, options).subscribe(response => {
      this.matMovieList = new MatTableDataSource(response);
      this.matMovieList.sort = this.movieSort;
    }, error => {
      console.log(error);
    });
  }

  OnSelectItem(itemType: string, itemSpecID: string){
    this.router.navigate(['/detail', itemType, itemSpecID])
  }
}
