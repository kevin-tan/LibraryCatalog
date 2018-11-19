import {Component, OnInit, ViewChild} from '@angular/core';
import {Magazine} from "../catalog/dto/item-specification/magazine";
import {HttpClient, HttpHeaders} from "@angular/common/http";
import {MatSort, MatTableDataSource} from '@angular/material';
import {Router} from "@angular/router";
import {NgForm} from '@angular/forms';

@Component({
  selector: 'app-catalog',
  templateUrl: './magazineSearch.component.html',
  styleUrls: ['./magazineSearch.component.css']
})
export class magazineSearchComponent implements OnInit {

  displayMagazineColumns: string[] = ['title', 'publisher', 'pubDate', 'language', 'isbn10', 'isbn13'];
  matMagazineList: MatTableDataSource<Magazine>;

  @ViewChild('magazineSort') magazineSort: MatSort;
  @ViewChild('magazineForm') magazineForm: NgForm;

  constructor(private http: HttpClient, private router:Router) {
  }

  ngOnInit() {
    this.getAllMagazines();
  }

  getAllMagazines(): void {
    this.http.get<Array<Magazine>>('http://localhost:8080/user/catalog/getAll/magazine', {withCredentials: true}).subscribe(response => {
      this.magazineForm.resetForm();
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
    });

    let headers = new HttpHeaders({"Content-Type": "application/json"});
    let options = {headers: headers, withCredentials: true};
    this.http.post<Array<Magazine>>('http://localhost:8080/user/catalog/search/magazine', body, options).subscribe(response => {
      this.magazineForm.resetForm();
      this.matMagazineList = new MatTableDataSource(response);
      this.matMagazineList.sort = this.magazineSort;
    }, error => {
      console.log(error);
    });
  }

  OnSelectItem(itemType: string, itemSpecID: string){
    this.router.navigate(['/detail', itemType, itemSpecID])
  }
}
