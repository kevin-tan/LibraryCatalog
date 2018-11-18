import { Component, OnInit } from '@angular/core';
import {ActivatedRoute} from "@angular/router";
import {LoanableItem} from "../catalog/dto/loanableItem";
import {ItemSpecification} from "../catalog/dto/item-specification/item_specification";
import {HttpClient} from "@angular/common/http";
import {Magazine} from "../catalog/dto/item-specification/magazine";

@Component({
  selector: 'app-details',
  templateUrl: './details.component.html',
  styleUrls: ['./details.component.css']
})
export class DetailsComponent implements OnInit {

  constructor(private route: ActivatedRoute, private http: HttpClient) { }

  type: string;
  specID: number;
  displayBookColumns: string[] = ['id', 'title', 'author', 'pages', 'format', 'publisher', 'isbn10', 'isbn13', 'pubDate', 'language', 'availability'];
  displayMovieColumns: string[] = ['id', 'title', 'language', 'producers', 'actors', 'dubbed', 'subtitles', 'releaseDate' ,'runTime', 'availability'];
  displayMagazineColumns: string[] = ['id', 'title', 'publisher', 'pubDate', 'language', 'isbn10', 'isbn13', 'availability'];
  displayMusicColumns: string[] = ['id', 'title', 'artist', 'label', 'type', 'asin', 'releaseDate', 'availability'];

  loanables: LoanableItem[];
  magazines: ItemSpecification[];

  ngOnInit() {
    this.type = this.route.snapshot.paramMap.get('type');
    this.specID = parseInt(this.route.snapshot.paramMap.get('id'));
    this.getAllItems();
  }

  getAllItems(){
    if(this.type == 'Magazine'){
      this.http.get<ItemSpecification[]>('http://localhost:8080/user/catalog/getAll/' + this.type +'/' + this.specID, {withCredentials: true},)
        .subscribe(response => {
          this.magazines = response;
        }, error => {
          console.log(error);
        });
    }else{
      this.http.get<LoanableItem[]>('http://localhost:8080/user/catalog/getAll/' + this.type +'/' + this.specID, {withCredentials: true},)
        .subscribe(response => {
          this.loanables = response;
        }, error => {
          console.log(error);
        });
    }
  }

  addItemToCart(){

  }

}
