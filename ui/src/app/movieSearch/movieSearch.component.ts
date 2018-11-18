import {Component, OnInit, ViewChild} from '@angular/core';
import {Movie} from "../catalog/dto/movie";
import {HttpClient, HttpHeaders} from "@angular/common/http";
import {HomeRedirectService} from "../home/home-redirect.service";
import {MatSort, MatTableDataSource} from '@angular/material';
import {NgForm} from '@angular/forms';

@Component({
  selector: 'app-catalog',
  templateUrl: './movieSearch.component.html',
  styleUrls: ['./movieSearch.component.css']
})
export class movieSearchComponent implements OnInit {

  displayMovieColumns: string[] = ['title', 'language', 'director', 'producers', 'actors', 'dubbed', 'subtitles', 'releaseDate' ,'runTime'];
  matMovieList: MatTableDataSource<Movie>;

  @ViewChild('movieSort') movieSort: MatSort;
  @ViewChild('movieForm') movieForm: NgForm;

  constructor(private http: HttpClient, private homeRedirectService: HomeRedirectService) { }

  ngOnInit() {
    this.getAllMovies();
  }

  logout(){
    let body = JSON.stringify({'email': sessionStorage.getItem('email')});
    this.http.post('http://localhost:8080/logout', body, {withCredentials:true}).subscribe(response => {
      this.homeRedirectService.redirect();
      sessionStorage.setItem('loggedIn', 'false');
      sessionStorage.setItem('email', '');
    }, error => {
      console.log(error);
    });
  }

  getAllMovies(): void {
    this.http.get<Array<Movie>>('http://localhost:8080/user/catalog/getAll/movie', {withCredentials: true}).subscribe(response => {
      this.movieForm.resetForm();
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
      this.matMovieList.sort = this.movieSort;
    }, error => {
      console.log(error);
    });
  }
}
