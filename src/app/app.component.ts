import { Component } from '@angular/core';
import { Router } from '@angular/router';
import { TokenStorageServiceService } from './services/token-storage-service.service';

@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.scss']
})
export class AppComponent {
  title = 'I am Romanian';
  private roles: string[] = [];
  isLoggedIn = false;
  showAdminBoard = false;
  // showModeratorBoard = false;
  username?: string;

  constructor(private tokenStorageService: TokenStorageServiceService, private router: Router) { }

  ngOnInit(): void {
    this.isLoggedIn = !!this.tokenStorageService.getToken();

    if (this.isLoggedIn) {
      const user = this.tokenStorageService.getAnswerLogin();
      this.roles = user.roles;

      this.showAdminBoard = this.roles.includes('ROLE_ADMIN');

      this.username = user.username;
    }
  }

  logout(): void {
    this.tokenStorageService.signOut();
    window.location.reload();
  }
}
