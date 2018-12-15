package com.ecommerce.microcommerce.web.controller;

import com.ecommerce.microcommerce.dao.ProductDao;
import com.ecommerce.microcommerce.model.Product;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;

import static org.junit.Assert.assertEquals;

@RunWith(SpringRunner.class)
@SpringBootTest
public class ProductControllerTest {

    @Autowired
    private ProductDao productDao;

    @Test
    public final void ajouterProduitTest() {
        ProductController productController = new ProductController();
        Product p1 = new Product(1, "Test Ordinateur portable", 350, 120);
        Product p2 = new Product(2, "Test Aspirateur Robot", 500, 200);

        List<Product> produits = productDao.findAll();

        productController.ajouterProduit(p1);
        productController.ajouterProduit(p2);

        assertEquals(2, produits.size());
    }
}
