import { Component, OnInit } from '@angular/core';
import {HttpClient} from '@angular/common/http';
import {Book} from '../catalog/dto/book';

@Component({
  selector: 'app-catalog',
  templateUrl: './bookSearch.component.html',
  styleUrls: ['./bookSearch.component.css']
})
export class bookSearchComponent implements OnInit {

  bookList: Array<Book> = [];

  constructor(private http: HttpClient) { }

  ngOnInit() {
    this.getAllBooks();
  }

  getAllBooks(): void {
    this.http.get<Array<Book>>('http://localhost:8080/user/catalog/getAll/book', {withCredentials: true}).subscribe(response => {
      this.bookList = response;
    }, error => {
      console.log(error);
    });
  }

}
