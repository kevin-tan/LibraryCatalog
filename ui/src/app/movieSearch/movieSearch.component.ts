import { Component, OnInit } from '@angular/core';
import {Movie} from "../catalog/dto/movie";
import {HttpClient, HttpHeaders} from "@angular/common/http";

@Component({
  selector: 'app-catalog',
  templateUrl: './movieSearch.component.html',
  styleUrls: ['./movieSearch.component.css']
})
export class movieSearchComponent implements OnInit {

  movieList: Array<Movie> = [];

  constructor(private http: HttpClient) { }

  ngOnInit() {
    this.getAllMovies();
  }

  getAllMovies(): void {
    this.http.get<Array<Movie>>('http://localhost:8080/user/catalog/getAll/movie', {withCredentials: true}).subscribe(response => {
      this.movieList = response;
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
    })

    let headers = new HttpHeaders({"Content-Type": "application/json"});
    let options = {headers: headers, withCredentials: true};
    this.http.post<Array<Movie>>('http://localhost:8080/user/catalog/search/movie', body, options).subscribe(response => {
      this.movieList = response;
    }, error => {
      console.log(error);
    });
  }
}
