import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { AboutComponent } from './about/about.component';
import { CartComponent } from './cart/cart.component';
import { ContactComponent } from './contact/contact.component';
import { HomeComponent } from './home/home.component';
import { IndividualProductComponent } from './individual-product/individual-product.component';
import { LoginRegisterComponent } from './login-register/login-register.component';
import { ProfileComponent } from './profile/profile.component';
import { WishlistComponent } from './wishlist/wishlist.component';

const routes: Routes = [
  {path: '', component: HomeComponent},
  {path: 'login', component: LoginRegisterComponent},
  {path: 'products/:id', component: IndividualProductComponent},
  // {path: 'admin', component: BoardAdminComponent},
  {path: 'profile', component: ProfileComponent},
  {path: 'cart', component: CartComponent},
  {path: 'wishlist', component: WishlistComponent},
  {path: 'products/sort/:id', component: HomeComponent},
  {path: 'contact', component: ContactComponent},
  {path: 'about', component: AboutComponent},
  // {path: 'products?name=:name', component:HomeComponent}
];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutingModule { }
// export const routingComponents = [HomeComponent, LoginRegisterComponent, BoardAdminComponent]