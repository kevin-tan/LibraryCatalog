import {Component, OnInit, ViewChild} from '@angular/core';
import {Music} from "../catalog/dto/item-specification/music";
import {HttpClient, HttpHeaders} from "@angular/common/http";
import {MatSort, MatTableDataSource} from '@angular/material';
import {Router} from "@angular/router";

@Component({
  selector: 'app-catalog',
  templateUrl: './musicSearch.component.html',
  styleUrls: ['./musicSearch.component.css']
})
export class musicSearchComponent implements OnInit {

  displayMusicColumns: string[] = ['title', 'artist', 'label', 'type', 'asin', 'releaseDate'];
  matMusicList: MatTableDataSource<Music>;

  @ViewChild('musicSort') musicSort: MatSort;

  constructor(private http: HttpClient, private router:Router) { }

  ngOnInit() {
    this.getAllMusics();
  }

  getAllMusics(): void {
    this.http.get<Array<Music>>('http://localhost:8080/user/catalog/getAll/music', {withCredentials: true}).subscribe(response => {
      this.matMusicList = new MatTableDataSource(response);
      this.matMusicList.sort = this.musicSort;
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
    })

    let headers = new HttpHeaders({"Content-Type": "application/json"});
    let options = {headers: headers, withCredentials: true};
    this.http.post<Array<Music>>('http://localhost:8080/user/catalog/search/music', body, options).subscribe(response => {
      this.matMusicList = new MatTableDataSource(response);
      this.matMusicList.sort = this.musicSort;
    }, error => {
      console.log(error);
    });
  }

  OnSelectItem(itemType: string, itemSpecID: string){
    this.router.navigate(['/detail', itemType, itemSpecID])
  }
}
