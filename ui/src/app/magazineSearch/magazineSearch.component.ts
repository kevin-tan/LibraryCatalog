import { Component, OnInit } from '@angular/core';
import {Magazine} from "../catalog/dto/Magazine";
import {HttpClient, HttpHeaders} from "@angular/common/http";

@Component({
  selector: 'app-catalog',
  templateUrl: './magazineSearch.component.html',
  styleUrls: ['./magazineSearch.component.css']
})
export class magazineSearchComponent implements OnInit {

  magazineList: Array<Magazine> = [];

  constructor(private http: HttpClient) {
  }

  ngOnInit() {
    this.getAllMagazines();
  }

  getAllMagazines(): void {
    this.http.get<Array<Magazine>>('http://localhost:8080/user/catalog/getAll/magazine', {withCredentials: true}).subscribe(response => {
      this.magazineList = response;
    }, error => {
      console.log(error);
    });
  }

  searchMagazines(title: string,
                  publisher: string,
                  pubDate: string,
                  language: string,
                  isbn10: string,
                  isbn13: string) {

    let body = JSON.stringify({
      "title": title,
      "publisher": publisher,
      "pubDate": pubDate,
      "language": language,
      "isbn10": isbn10,
      "isbn13": isbn13
    })

    let headers = new HttpHeaders({"Content-Type": "application/json"});
    let options = {headers: headers, withCredentials: true};
    this.http.post<Array<Magazine>>('http://localhost:8080/user/catalog/search/magazine', body, options).subscribe(response => {
      this.magazineList = response;
    }, error => {
      console.log(error);
    });
  }
}
