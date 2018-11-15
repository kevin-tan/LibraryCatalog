import { Component, OnInit } from '@angular/core';
import {Music} from "../catalog/dto/music";
import {HttpClient, HttpHeaders} from "@angular/common/http";
import {HomeRedirectService} from "../home/home-redirect.service";

@Component({
  selector: 'app-catalog',
  templateUrl: './musicSearch.component.html',
  styleUrls: ['./musicSearch.component.css']
})
export class musicSearchComponent implements OnInit {

  musicList: Array<Music> = [];

  constructor(private http: HttpClient, private homeRedirectService: HomeRedirectService) { }

  ngOnInit() {
    this.getAllMusics();
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

  getAllMusics(): void {
    this.http.get<Array<Music>>('http://localhost:8080/user/catalog/getAll/music', {withCredentials: true}).subscribe(response => {
      this.musicList = response;
    }, error => {
      console.log(error);
    });
  }
  searchMusics(title: string,
               artist: string,
               type: string,
               releaseDate: string,
               label: string,
               asin: string) {

    let body = JSON.stringify({
      "title": title,
      "artist": artist,
      "releaseDate": releaseDate,
      "label": label,
      "type": type,
      "asin": asin
    })

    let headers = new HttpHeaders({"Content-Type": "application/json"});
    let options = {headers: headers, withCredentials: true};
    this.http.post<Array<Music>>('http://localhost:8080/user/catalog/search/music', body, options).subscribe(response => {
      this.musicList = response;
    }, error => {
      console.log(error);
    });
  }
}
