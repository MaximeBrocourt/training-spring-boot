package com.ecommerce.microcommerce;

import com.ecommerce.microcommerce.dao.ProductDao;
import com.ecommerce.microcommerce.model.Product;
import com.ecommerce.microcommerce.web.controller.ProductController;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.client.RestClientTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.converter.json.MappingJacksonValue;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;

@RunWith(SpringRunner.class)
@SpringBootTest
public class DatabaseDAOTests {

    @Autowired
    private ProductDao produits;

	@Test
    public void testFindAll() {
        produits.findAll();
        Assert.assertNotNull(produits);
    }

    @Test
    public void testFindById(){
	    int id=1;
	    Product prod = produits.findById(id);
        Assert.assertNotNull(prod);
        Assert.assertEquals(prod.getNom(), "Ordinateur portable");
        Assert.assertEquals(prod.getId(), 1);
        Assert.assertEquals(prod.getPrixAchat(), 120);
        Assert.assertEquals(prod.getPrix(), 350);
    }

    @Test
    public void testChangeProductParams() {
	    int id=2;
	    Product prod = produits.findById(id);
	    Assert.assertNotNull(prod);
	    Assert.assertEquals(prod.getNom(), "Aspirateur Robot");
        Assert.assertEquals(prod.getId(), 2);
        Assert.assertEquals(prod.getPrixAchat(), 200);
        Assert.assertEquals(prod.getPrix(), 500);

        //Change params
        prod.setId(10);
        prod.setNom("Nouveau Aspirateur");
        prod.setPrix(300);
        prod.setPrixAchat(800);

        Assert.assertEquals(prod.getNom(), "Nouveau Aspirateur");
        Assert.assertEquals(prod.getId(), 10);
        Assert.assertEquals(prod.getPrixAchat(), 800);
        Assert.assertEquals(prod.getPrix(), 300);
    }


    @Test
    public void testFindByPrix(){
        List<Product> products = produits.findByPrixGreaterThan(500);
        Assert.assertNotNull(products);
    }

    @Test
    public void testFindByNom(){
        List<Product> products = produits.findByNomLike("Aspirateur Robot");
        Assert.assertNotNull(products);
    }

    @Test
    public void testChercherUnProduitCher(){
        List<Product> products = produits.chercherUnProduitCher(500);
        Assert.assertNotNull(products);
    }
}
