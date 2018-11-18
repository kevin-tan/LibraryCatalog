import { Component, OnInit } from '@angular/core';
import {ActivatedRoute, Router} from "@angular/router";
import {LoanableItem} from "../catalog/dto/loanableItem";
import {ItemSpecification} from "../catalog/dto/item-specification/item_specification";
import {HttpClient, HttpHeaders} from "@angular/common/http";
import {Magazine} from "../catalog/dto/item-specification/magazine";

@Component({
  selector: 'app-details',
  templateUrl: './details.component.html',
  styleUrls: ['./details.component.css']
})
export class DetailsComponent implements OnInit {

  constructor(private route: ActivatedRoute, private http: HttpClient, private router:Router) { }

  type: string;
  specID: number;
  displayBookColumns: string[] = ['id', 'title', 'author', 'pages', 'format', 'publisher', 'isbn10', 'isbn13', 'pubDate', 'language', 'availability'];
  displayMovieColumns: string[] = ['id', 'title', 'language', 'producers', 'actors', 'dubbed', 'subtitles', 'releaseDate' ,'runTime', 'availability'];
  displayMagazineColumns: string[] = ['id', 'title', 'publisher', 'pubDate', 'language', 'isbn10', 'isbn13', 'availability'];
  displayMusicColumns: string[] = ['id', 'title', 'artist', 'label', 'type', 'asin', 'releaseDate', 'availability'];

  loanables: LoanableItem[];
  magazines: ItemSpecification[];
  availableCounter: number = 0;

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
          for(let item of this.loanables){
            if(item.available){
               this.availableCounter +=1;
            }
          }
        }, error => {
          console.log(error);
        });
    }
  }

  addItemToCart(loanableItem: LoanableItem){
    let json = {};
    json["id"] = loanableItem.id;
    json["available"] = loanableItem.available;
    json["client"] = loanableItem.client;
    json["spec"] = loanableItem.spec;
    json["type"] = loanableItem.type;
    let final = {["LoanableItem"]:json};

    let body = JSON.stringify(final);

    let headers = new HttpHeaders({'Content-Type': 'application/json'});
    let options = {headers: headers, withCredentials: true};

    this.http.post('http://localhost:8080/client/cart/' + sessionStorage.getItem('user_id') + "/add", body, options)
      .subscribe(response => {
        this.router.navigate(['/cart']);
      }, error => {
        console.log(error);
      });
  }

}
