import {Component, OnInit, ViewChild} from '@angular/core';
import {Magazine} from "../catalog/dto/Magazine";
import {HttpClient, HttpHeaders} from "@angular/common/http";
import {HomeRedirectService} from "../home/home-redirect.service";
import {MatSort, MatTableDataSource} from '@angular/material';

@Component({
  selector: 'app-catalog',
  templateUrl: './magazineSearch.component.html',
  styleUrls: ['./magazineSearch.component.css']
})
export class magazineSearchComponent implements OnInit {

  displayMagazineColumns: string[] = ['title', 'publisher', 'pubDate', 'language', 'isbn10', 'isbn13'];
  matMagazineList: MatTableDataSource<Magazine>;

  @ViewChild('magazineSort') magazineSort: MatSort;

  constructor(private http: HttpClient, private homeRedirectService: HomeRedirectService) {
  }

  ngOnInit() {
    this.getAllMagazines();
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

  getAllMagazines(): void {
    this.http.get<Array<Magazine>>('http://localhost:8080/user/catalog/getAll/magazine', {withCredentials: true}).subscribe(response => {
      this.matMagazineList = new MatTableDataSource(response);
      this.matMagazineList.sort = this.magazineSort;
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
      this.matMagazineList = new MatTableDataSource(response);
      this.matMagazineList.sort = this.magazineSort;
    }, error => {
      console.log(error);
    });
  }
}
