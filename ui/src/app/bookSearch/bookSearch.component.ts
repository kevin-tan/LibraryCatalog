import {Component, OnInit, ViewChild} from '@angular/core';
import {HttpClient, HttpHeaders} from '@angular/common/http';
import {Book} from '../catalog/dto/book';
import {HomeRedirectService} from "../home/home-redirect.service";
import {MatSort, MatTableDataSource} from '@angular/material';

@Component({
  selector: 'app-catalog',
  templateUrl: './bookSearch.component.html',
  styleUrls: ['./bookSearch.component.css']
})
export class bookSearchComponent implements OnInit {

  displayBookColumns: string[] = ['title', 'author', 'pages', 'format', 'publisher', 'isbn10', 'isbn13', 'pubDate', 'language'];
  matBookList: MatTableDataSource<Book>;

  constructor(private http: HttpClient, private homeRedirectService: HomeRedirectService) { }

  @ViewChild('bookSort') bookSort: MatSort;

  ngOnInit() {
    this.getAllBooks();
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

  getAllBooks(): void {
    this.http.get<Array<Book>>('http://localhost:8080/user/catalog/getAll/book', {withCredentials: true}).subscribe(response => {
      this.matBookList = new MatTableDataSource(response);
      this.matBookList.sort = this.bookSort;
    }, error => {
      console.log(error);
    });
  }
  searchBooks(title: string,
              author: string,
              publisher: string,
              pubDate: string,
              language: string,
              format: string,
              isbn10: string,
              isbn13: string) {

    let body = JSON.stringify({
      "title": title,
      "author": author,
      "publisher": publisher,
      "pubDate": pubDate,
      "language": language,
      "format": format,
      "isbn10": isbn10,
      "isbn13": isbn13
    })

    let headers = new HttpHeaders({"Content-Type": "application/json"});
    let options = {headers: headers, withCredentials: true};
    this.http.post<Array<Book>>('http://localhost:8080/user/catalog/search/book', body, options).subscribe(response => {
      this.matBookList = new MatTableDataSource(response);
      this.matBookList.sort = this.bookSort;
    }, error => {
      console.log(error);
    });
  }

}
