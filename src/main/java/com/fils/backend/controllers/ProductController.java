package com.fils.backend.controllers;

import com.fils.backend.domain.*;
import com.fils.backend.repositories.ProductTypeRepository;
import com.fils.backend.security.JwtUtil;
import com.fils.backend.services.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestController
@RequestMapping("/products")
public class ProductController {
    @Autowired
    ProductService productService;

    @Autowired
    JwtUtil jwtUtil;

    @Autowired
    UserService userService;

    @Autowired
    ReviewService reviewService;

    @Autowired
    ProductTypeRepository categoryRepository;

    @Autowired
    private EmailTokenService emailVerificationTokenService;

    @Autowired
    private WishlistService wishlistService;

    @Autowired
    private OrderService orderService;

    @PostMapping("")
    public ResponseEntity<Product> createProduct(@RequestBody Product product, @RequestHeader("Authorization") String auth) {
        try {
            String jwtToken = auth.substring(7);
            String username = jwtUtil.extractUsername(jwtToken);
            Optional<User> userByUsername = userService.getUserByUsername(username);

            if(userByUsername.isPresent() && userByUsername.get().getRoles().contains("ROLE_ADMIN") && product!=null) {
                Product p = new Product();
                p.setName(product.getName());
                p.setType(product.getType());
                p.setDescription(product.getDescription());
                p.setPrice(product.getPrice());
                p.setStock(product.getStock());
                p.setImageUrl(product.getImageUrl());
                p.setUser(userByUsername.get());

                productService.saveProduct(p);
                return new ResponseEntity<>(HttpStatus.CREATED);
            } else{
                return new ResponseEntity<>(HttpStatus.FORBIDDEN);
            }

        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }

        //cod pt cazul in care produsul nou are acelasi nume cu un alt produs al aceluiasi furnizor
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity deleteProductById(@PathVariable("id") Long id) {
        Product product = productService.getProductById(id);
        productService.deleteProduct(product);
        return ResponseEntity.ok("DELETED: " + product);
    }

    @GetMapping("/sort/{categoryId}/{pageNo}")
    public ResponseEntity<List<Product>> sortProductsByType(
            @PathVariable Long categoryId, @PathVariable(required = false) int pageNo){
        List<Product> sortedProducts = productService.getProductsByCategory(categoryId, pageNo);
        if(!sortedProducts.isEmpty()) {
            return new ResponseEntity<>(sortedProducts, HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }


    @GetMapping("/page/{pageNo}")
    public ResponseEntity<List<Product>> getProducts(@RequestParam(required = false) String name, @PathVariable(required = false) int pageNo) {
        try {
            List<Product> products = new ArrayList<>();

            if (name == null)
                productService.getProducts(pageNo).forEach(products::add);
            else
                productService.getProductsByName(name).forEach(products::add);

            if (products.isEmpty()) {
                return new ResponseEntity<>(HttpStatus.NO_CONTENT);
            }
            return new ResponseEntity<>(products, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<Product> getProductById(@PathVariable("id") Long id){
        try{
            Product product = productService.getProductById(id);
            product.setTotalRating(productService.computeRating(reviewService.getByProductId(id)));

            return new ResponseEntity<>(product,HttpStatus.OK);
        } catch (Exception e){
            return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping("/product-category")
    public ResponseEntity<List<ProductType>> getAllCategories(){
        return new ResponseEntity<>(categoryRepository.findAll(),HttpStatus.OK);
    }

    @PutMapping("/update")
    public ResponseEntity updateProduct(@RequestBody Product product, @RequestHeader("Authorization") String auth){
        try {
            String jwtToken = auth.substring(7);
            String username = jwtUtil.extractUsername(jwtToken);
            Optional<User> userByUsername = userService.getUserByUsername(username);
            List<User> allUsers = userService.getAllUsers();

            if (userByUsername.isPresent()) {
                Product dbProduct = productService.getProductById(product.getId());
                if (dbProduct!=null) {
                    if (dbProduct.getUser().getId().equals(userByUsername.get().getId())) {
                        // USER CAN ONLY EDIT PRICE and STOCK
                        dbProduct.setPrice(product.getPrice());
                        dbProduct.setStock(product.getStock());
                        productService.saveProduct(dbProduct);
                        //ONLY USERS WHO HAVE IT IN WISHLIST RECEIVES EMAIL
                        for(User u: allUsers){
                            List<WishlistItem> wishlistItems = wishlistService.getWishlistItems(u);
                            for(WishlistItem wli: wishlistItems)
                                if(wli.getProduct().getId()==product.getId()){
                                    emailVerificationTokenService.sendNewsletter(u, product);
                                }
                        }
                        return ResponseEntity.ok().body("Product updated successfully");
                    } else {
                        return ResponseEntity.status(HttpStatus.FORBIDDEN).body("ERROR: MissMatch Product-UserId with UserId (NOT_FOUND)");
                    }
                } else {
                    return ResponseEntity.status(HttpStatus.NOT_FOUND).body("ERROR: Product Couldn't be found !");
                }
            } else {
                return ResponseEntity.status(HttpStatus.FORBIDDEN).body("ERROR: MissMatch JWT TOKEN with User (NOT_FOUND)");
            }

        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE).body("ERROR: EmailService failed !!");
        }
    }

    @GetMapping("/recommendations")
    public ResponseEntity<List<Product>> getAdminRecommendations(@RequestHeader("Authorization") String auth){
        try {
            String jwtToken = auth.substring(7);
            String username = jwtUtil.extractUsername(jwtToken);
            Optional<User> userByUsername = userService.getUserByUsername(username);

            if (userByUsername.isPresent() && userByUsername.get().getRoles().contains("ROLE_ADMIN")) {
                //Get all products for this seller
                List<Product> allProdForAdmin = productService.getAllByUser(userByUsername.get());
                //Get the three best ranked products
                List<Product> theBestProd = productService.threeBestRanked(allProdForAdmin);
                return ResponseEntity.status(HttpStatus.OK).body(theBestProd);
                //Get all products from the other seller

                //Get the three best ranked products from them
            } else return ResponseEntity.status(HttpStatus.NO_CONTENT).body(null);
        }catch (Exception e){
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE).body(null);
        }
    }

    @GetMapping("/user-recommendations")
    public ResponseEntity<List<Product>> getUserRecommendations(@RequestHeader("Authorization") String auth){
        try {
            //get credentials
            String jwtToken = auth.substring(7);
            String username = jwtUtil.extractUsername(jwtToken);
            Optional<User> userByUsername = userService.getUserByUsername(username);

            if (userByUsername.isPresent() && userByUsername.get().getRoles().contains("ROLE_GUEST")) {
                //see customer's other orders
                List<Order> ordersByUser = orderService.getOrdersByUser(userByUsername.get());
                List<Product> productListFromOrders = new ArrayList<>();

                //get all products from his orders
                for(Order o: ordersByUser) {
                    for (Product p: o.getProductsFromCart()) {
                        productListFromOrders.add(p);
                    }
                }

                //get categories of products from other orders
                List<ProductType> categories = new ArrayList<>();
                for(Product p: productListFromOrders){
                    if(categories.contains(p.getType())){
                        continue;
                    }else {
                        categories.add(p.getType());
                    }
                }

                System.out.println(categories);
                //return product with same categories and maximum rating
                List<Product> allProducts = productService.getProducts();
                List<Product> recommendations = new ArrayList<>();

                for(ProductType category: categories){
                    for(Product product: allProducts){
                        if(product.getType().equals(category) && product.getTotalRating()>4.0){
                            recommendations.add(product);
                        }
                    }
                }
                return ResponseEntity.status(HttpStatus.OK).body(recommendations);
            }
            else return ResponseEntity.status(HttpStatus.NO_CONTENT).body(null);
        }catch (Exception e){
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE).body(null);
        }
    }
}
