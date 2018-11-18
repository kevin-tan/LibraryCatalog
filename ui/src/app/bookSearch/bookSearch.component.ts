import {Component, OnInit, ViewChild} from '@angular/core';
import {HttpClient, HttpHeaders} from '@angular/common/http';
import {Book} from '../catalog/dto/item-specification/book';
import {MatSort, MatTableDataSource} from '@angular/material';
import {Router} from "@angular/router";

@Component({
  selector: 'app-catalog',
  templateUrl: './bookSearch.component.html',
  styleUrls: ['./bookSearch.component.css']
})
export class bookSearchComponent implements OnInit {

  displayBookColumns: string[] = ['title', 'author', 'pages', 'format', 'publisher', 'isbn10', 'isbn13', 'pubDate', 'language'];
  matBookList: MatTableDataSource<Book>;

  constructor(private http: HttpClient, private router:Router) { }

  @ViewChild('bookSort') bookSort: MatSort;

  ngOnInit() {
    this.getAllBooks();
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
    });

    let headers = new HttpHeaders({"Content-Type": "application/json"});
    let options = {headers: headers, withCredentials: true};
    this.http.post<Array<Book>>('http://localhost:8080/user/catalog/search/book', body, options).subscribe(response => {
      this.matBookList = new MatTableDataSource(response);
      this.matBookList.sort = this.bookSort;
    }, error => {
      console.log(error);
    });
  }
  OnSelectItem(itemType: string, itemSpecID: string){
    this.router.navigate(['/detail', itemType, itemSpecID])
  }
}
