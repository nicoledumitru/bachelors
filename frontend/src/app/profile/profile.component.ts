import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { TokenStorageServiceService } from '../services/token-storage-service.service';

@Component({
  selector: 'app-profile',
  templateUrl: './profile.component.html',
  styleUrls: ['./profile.component.scss']
})
export class ProfileComponent implements OnInit {
  currentUser: any;

  constructor(private tokenService: TokenStorageServiceService, private router: Router) { }

  ngOnInit(): void {
    this.currentUser = this.tokenService.getAnswerLogin();
  }

  logout(): void {
    this.tokenService.signOut();
    // window.location.reload();
    this.router.navigateByUrl('');
  }

}
