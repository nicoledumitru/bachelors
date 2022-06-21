import { NgModule } from '@angular/core';
import { BrowserModule } from '@angular/platform-browser';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';
import { CommonModule } from '@angular/common';
import { HttpClientModule } from '@angular/common/http';
import { MatDialogModule, MAT_DIALOG_DEFAULT_OPTIONS } from '@angular/material/dialog';
import { BrowserAnimationsModule } from '@angular/platform-browser/animations';
import { NgChartsModule } from 'ng2-charts';
import { RatingModule } from 'ngx-bootstrap/rating';


import { AppRoutingModule } from './app-routing.module';
import { AppComponent } from './app.component';
import { LoginRegisterComponent } from './login-register/login-register.component';
import { NavbarComponent } from './navbar/navbar.component';
import { HomeComponent } from './home/home.component';
import { AboutComponent } from './about/about.component';
import { ContactComponent } from './contact/contact.component';
import { HeaderComponent } from './header/header.component';
import { MainContainerComponent } from './main-container/main-container.component';
import { ProductListComponent } from './home/product-list/product-list.component';
import { ProductItemComponent } from './home/product-list/product-item/product-item.component';
import { AddProductModalComponent } from './add-product-modal/add-product-modal.component';
import { authInterceptorProviders } from './_helpers/auth.interceptor';
import { ProfileComponent } from './profile/profile.component';
import { CartComponent } from './cart/cart.component';
import { CartItemComponent } from './cart/cart-item/cart-item.component';
import { WishlistComponent } from './wishlist/wishlist.component';
import { IndividualProductComponent } from './individual-product/individual-product.component';
import { ProductCategoryMenuComponent } from './product-category-menu/product-category-menu.component';
import { CardStatsComponent } from './profile/card-stats/card-stats.component';
import { FooterComponent } from './footer/footer.component';
import { RecommendationsListComponent } from './profile/recommendations-list/recommendations-list.component';
import { RecommendationsItemComponent } from './profile/recommendations-list/recommendations-item/recommendations-item.component';

@NgModule({
  declarations: [
    AppComponent,
    LoginRegisterComponent,
    NavbarComponent,
    HomeComponent,
    AboutComponent,
    ContactComponent,
    HeaderComponent,
    MainContainerComponent,
    ProductListComponent,
    ProductItemComponent,
    AddProductModalComponent,
    ProfileComponent,
    CartComponent,
    CartItemComponent,
    WishlistComponent,
    IndividualProductComponent,
    ProductCategoryMenuComponent,
    CardStatsComponent,
    FooterComponent,
    RecommendationsListComponent,
    RecommendationsItemComponent
  ],
  imports: [
    BrowserModule,
    AppRoutingModule,
    BrowserModule,
    FormsModule,
    ReactiveFormsModule,
    CommonModule,
    HttpClientModule,
    MatDialogModule,
    // NoopAnimationsModule,
    NgChartsModule,
    BrowserAnimationsModule,
    RatingModule.forRoot()
  ],
  providers: [authInterceptorProviders,
  {provide: MAT_DIALOG_DEFAULT_OPTIONS, useValue: {hasBackdrop: false}}],
  bootstrap: [AppComponent]
})
export class AppModule { }
