import {Component, OnInit} from "@angular/core";
import {HomeRedirectService} from "./home-redirect.service";
import {HttpClient} from "@angular/common/http";

@Component({
  selector: 'app-home',
  templateUrl: './home.component.html',
  styleUrls: ['./home.component.css']
})

export class HomeComponent implements OnInit{
  ngOnInit() {
  }

  constructor(private http:HttpClient, private homeRedirectService: HomeRedirectService){

  }

  logout(): void {
    let body = JSON.stringify({"email": sessionStorage.getItem("email")})
    this.http.post('http://localhost:8080/logout', body).subscribe(response => {
      this.homeRedirectService.redirect();
      sessionStorage.setItem("loggedIn", "false")
      sessionStorage.setItem("email", "")
    })
  }

  searchAllTitle(searchTitle: string) {
    this.http.get('http://localhost:8080/catalog/'+searchTitle).subscribe(response => {

    })
  }

  getAllCatalog() {
    this.http.get('http://localhost:8080/catalog/searchAll').subscribe(response => {

    })
  }
}
