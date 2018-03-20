import { Component, OnInit } from '@angular/core';
import { AuthService } from './../auth/auth.service';

@Component({
  selector: 'app-user',
  templateUrl: './user.component.html',
  styleUrls: ['./user.component.css']
})
export class UserComponent implements OnInit {

  userInfo: any

  constructor(public auth: AuthService) {
    auth.userInfo((err: string, userInfo: any) => {
      if (err) {

      } else if (userInfo) {
        console.log(JSON.stringify(userInfo));
        this.userInfo = userInfo;
      }
    });
  }

  ngOnInit() {
  }

  checkSession(): void {
    console.log("check session clicked");
  }


}
