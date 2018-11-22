import {Component, OnInit, ViewChild} from '@angular/core';
import {HttpClient, HttpHeaders} from '@angular/common/http';
import {Book} from '../catalog/dto/item-specification/book';
import {MatSort, MatTableDataSource} from '@angular/material';
import {NgForm} from '@angular/forms';
import {Router} from "@angular/router";

@Component({
  selector: 'app-catalog',
  templateUrl: './bookSearch.component.html',
  styleUrls: ['./bookSearch.component.css']
})
export class bookSearchComponent implements OnInit {

  values = ['PAPERBACK', 'HARDCOVER'];
  selectedOption = this.values[0];
  displayBookColumns: string[] = ['title', 'author', 'pages', 'format', 'publisher', 'isbn10', 'isbn13', 'pubDate', 'language', 'quantity'];
  bookList: Book[];
  matBookList: MatTableDataSource<Book>;

  constructor(private http: HttpClient, private router:Router) { }

  @ViewChild('bookSort') bookSort: MatSort;
  @ViewChild('bookForm') bookForm: NgForm;

  ngOnInit() {
    this.getAllBooks();
  }

  getAllBooks(): void {
    this.http.get<Array<Book>>('http://localhost:8080/user/catalog/getAll/book', {withCredentials: true}).subscribe(response => {
      this.bookForm.resetForm();
      this.matBookList = new MatTableDataSource(response);
      this.bookList = response;
      this.matBookList.sort = this.bookSort;

      this.getAllInventory();
    }, error => {
      console.log(error);
    });
  }
  searchBooks(title: string,
              author: string,
              publisher: string,
              pubDate: string,
              language: string,
              isbn10: string,
              isbn13: string) {

    let body = JSON.stringify({
      "title": title,
      "author": author,
      "publisher": publisher,
      "pubDate": pubDate,
      "language": language,
      "format": this.selectedOption === null ? "" : this.selectedOption,
      "isbn10": isbn10,
      "isbn13": isbn13
    });

    let headers = new HttpHeaders({"Content-Type": "application/json"});
    let options = {headers: headers, withCredentials: true};
    this.http.post<Array<Book>>('http://localhost:8080/user/catalog/search/book', body, options).subscribe(response => {
      this.bookForm.resetForm();
      this.matBookList = new MatTableDataSource(response);
      this.bookList = response;
      this.matBookList.sort = this.bookSort;

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
    }, error => {
      console.log(error);
    });
  }

  OnSelectItem(itemType: string, itemSpecID: string){
    this.router.navigate(['/detail', itemType, itemSpecID])
  }
}
