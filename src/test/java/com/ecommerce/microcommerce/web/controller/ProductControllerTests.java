package com.ecommerce.microcommerce;

import com.ecommerce.microcommerce.dao.ProductDao;
import com.ecommerce.microcommerce.model.Product;
import com.ecommerce.microcommerce.web.controller.ProductController;
import com.google.gson.Gson;
import io.restassured.module.mockmvc.specification.MockMvcRequestSpecification;
import org.apache.http.client.methods.HttpPost;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.BDDMockito;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.ResultMatcher;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.web.context.WebApplicationContext;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.standaloneSetup;

@RunWith(SpringRunner.class)
@SpringBootTest
public class ProductControllerTests {
    @MockBean
    private ProductDao produits;

    MockMvc mockMvc;

    @Autowired
    protected WebApplicationContext wac;

    @Autowired
    ProductController productController;

    /**
     * List of samples products
     */
    private List<Product> prod;

    @Before
    public void setup() throws Exception {
        this.mockMvc = standaloneSetup(this.productController).build();// Standalone context

        // mockMvc = MockMvcBuilders.webAppContextSetup(wac)
        Product prod1 = new Product(1 , "Produit1" , 100, 200);
        Product prod2 = new Product(2, "Produit2", 200, 500);
        Product prod3 = new Product(3, "Produit3", 200, 0);

        prod = new ArrayList<>();
        prod.add(prod1);
        prod.add(prod2);
        prod.add(prod3);
    }

    @Test
    public void testAjouterUnProduits() throws Exception {
        Product newProduct = new Product(15, "NewProduct", 1000, 666);
        given(produits.save(Mockito.any(Product.class)))
                .willReturn(newProduct);

        Gson gsonBuilder = new Gson();
        String newProductAsJson = gsonBuilder.toJson(newProduct);

        mockMvc.perform(
                post("/Produits")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(newProductAsJson))
                .andExpect(status().isCreated())
                .andReturn();
    }

    @Test
    public void testUpdateProduits() throws Exception {
        Product newProduct = new Product(1, "UpdateProduct", 1000, 666);
        given(produits.save(Mockito.any(Product.class)))
                .willReturn(newProduct);

        Gson gsonBuilder = new Gson();
        String newProductAsJson = gsonBuilder.toJson(newProduct);

        mockMvc.perform(
                put("/Produits")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(newProductAsJson))
                .andExpect(status().isOk())
                .andReturn();
    }

    @Test
    public void tesDeleteProduits() throws Exception {
        mockMvc.perform(
                delete("/Produits/1")
                .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk());
    }

    @Test
    public void testTriProduct() throws Exception {
        // Mocking service
        when(produits.triProduct()).thenReturn(prod);
        mockMvc.perform(get("/produits/tri").contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andDo(print())
                .andReturn();
    }

    @Test
    public void testChercherUnProduitCher() throws Exception {
        // Mocking service
        when(produits.chercherUnProduitCher(300)).thenReturn(prod);
        mockMvc.perform(get("/test/produits/300").contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andDo(print())
                .andReturn();
    }

    @Test
    public void testListProduits() throws Exception {
        // Mocking service
        when(produits.findAll()).thenReturn(prod);
        mockMvc.perform(get("/Produits").contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andDo(print())
                .andReturn();
    }

    @Test
    public void testAfficherProd() throws Exception {
            // Mocking service
            when(produits.findById(1)).thenReturn(prod.get(0));
            when(produits.findById(3)).thenReturn(prod.get(2));
            mockMvc.perform(get("/Produits/1").contentType(MediaType.APPLICATION_JSON))
                    .andDo(print())
                    .andExpect(status().isOk())
                    .andDo(print())
                    .andReturn();

            //Exception1
            mockMvc.perform(get("/Produits/5").contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isNotFound())
                .andDo(print())
                .andReturn();

            //Exception2
            mockMvc.perform(get("/Produits/3").contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isNotAcceptable())
                .andDo(print())
                .andReturn();
    }

    @Test
    public void testCalculerMargeProduit() throws Exception {
        // Mocking service
        when(produits.findAll()).thenReturn(prod);
        mockMvc.perform(get("/margeProduits").contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andDo(print())
                .andReturn();
    }
}
