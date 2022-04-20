import { Component, OnInit } from '@angular/core';
import { UserService } from '../services/user.service';
// import {MatDialogModule} from '@angular/material/dialog';
import { AddProductModalComponent } from './add-product-modal/add-product-modal.component';
import { MatDialog } from '@angular/material/dialog';

@Component({
  selector: 'app-board-admin',
  templateUrl: './board-admin.component.html',
  styleUrls: ['./board-admin.component.scss']
})
export class BoardAdminComponent implements OnInit {
  content?: string;

  constructor(private userService: UserService, public dialog: MatDialog) { }

  ngOnInit(): void {
    this.userService.getAdminBoard().subscribe({
      next: data => {
        this.content = data
      },
      error: err=>{
        this.content = JSON.parse(err.error).message;
      }
    });
  }

  openModalAddProduct(){
    const dialogRef = this.dialog.open(AddProductModalComponent, {
      height : '600px',
      width: '600px'
    });

    dialogRef.afterClosed().subscribe((result: any) => {
      console.log('The dialog was closed');
    });
  }

}
