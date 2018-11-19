import {Component, OnInit, ViewChild} from '@angular/core';
import {Music} from "../catalog/dto/item-specification/music";
import {HttpClient, HttpHeaders} from "@angular/common/http";
import {MatSort, MatTableDataSource} from '@angular/material';
import {Router} from "@angular/router";
import {NgForm} from '@angular/forms';

@Component({
  selector: 'app-catalog',
  templateUrl: './musicSearch.component.html',
  styleUrls: ['./musicSearch.component.css']
})
export class musicSearchComponent implements OnInit {

  displayMusicColumns: string[] = ['title', 'artist', 'label', 'type', 'asin', 'releaseDate', 'quantity'];
  musicList: Music[];
  matMusicList: MatTableDataSource<Music>;

  @ViewChild('musicSort') musicSort: MatSort;
  @ViewChild('musicForm') musicForm: NgForm;

  constructor(private http: HttpClient, private router:Router) { }

  ngOnInit() {
    this.getAllMusics();
  }

  getAllMusics(): void {
    this.http.get<Array<Music>>('http://localhost:8080/user/catalog/getAll/music', {withCredentials: true}).subscribe(response => {
      this.musicForm.resetForm();
      this.matMusicList = new MatTableDataSource(response);
      this.musicList = response;
      this.matMusicList.sort = this.musicSort;

      this.getAllInventory();
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
    });

    let headers = new HttpHeaders({"Content-Type": "application/json"});
    let options = {headers: headers, withCredentials: true};
    this.http.post<Array<Music>>('http://localhost:8080/user/catalog/search/music', body, options).subscribe(response => {
      this.musicForm.resetForm();
      this.matMusicList = new MatTableDataSource(response);
      this.musicList = response;
      this.matMusicList.sort = this.musicSort;

      this.getAllInventory();
    }, error => {
      console.log(error);
    });
  }

  getAllInventory() {
    this.http.get('http://localhost:8080/user/catalog/getAll/itemSpec/quantity', {withCredentials: true}).subscribe(response => {
      for (let music of this.musicList) {
        music.quantity = response['Music'][music.id] >= 0 ? response['Music'][music.id] : 0;
      }
    }, error => {
      console.log(error);
    });
  }

  OnSelectItem(itemType: string, itemSpecID: string){
    this.router.navigate(['/detail', itemType, itemSpecID])
  }
}
