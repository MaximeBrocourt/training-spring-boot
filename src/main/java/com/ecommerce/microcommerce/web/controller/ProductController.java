package com.ecommerce.microcommerce.web.controller;

import com.ecommerce.microcommerce.dao.ProductDao;
import com.ecommerce.microcommerce.model.Product;
import com.ecommerce.microcommerce.web.exceptions.ProduitGratuitException;
import com.ecommerce.microcommerce.web.exceptions.ProduitIntrouvableException;
import com.fasterxml.jackson.databind.ser.FilterProvider;
import com.fasterxml.jackson.databind.ser.impl.SimpleBeanPropertyFilter;
import com.fasterxml.jackson.databind.ser.impl.SimpleFilterProvider;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.json.MappingJacksonValue;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.validation.Valid;
import java.net.URI;
import java.util.ArrayList;
import java.util.List;


@Api(description = "API pour les opérations CRUD sur les produits.")

@RestController
public class ProductController {

    @Autowired
    private ProductDao productDao;

    //Récupérer la liste des produits
    @ApiOperation(value = "Récupère la liste des produits")
    @RequestMapping(value = "/Produits", method = RequestMethod.GET)
    public MappingJacksonValue listeProduits() {
        Iterable<Product> produits = productDao.findAll();
        SimpleBeanPropertyFilter monFiltre = SimpleBeanPropertyFilter.serializeAllExcept("prixAchat");
        FilterProvider listDeNosFiltres = new SimpleFilterProvider().addFilter("monFiltreDynamique", monFiltre);
        MappingJacksonValue produitsFiltres = new MappingJacksonValue(produits);
        produitsFiltres.setFilters(listDeNosFiltres);
        return produitsFiltres;
    }

    //Récupérer un produit par son Id
    @ApiOperation(value = "Récupère un produit grâce à son ID à condition que celui-ci soit en stock!")
    @GetMapping(value = "/Produits/{id}")
    public Product afficherUnProduit(@PathVariable int id) {
        Product produit = productDao.findById(id);
        if (produit == null)
            throw new ProduitIntrouvableException("Le produit avec l'id " + id + " est INTROUVABLE. Écran Bleu si je pouvais.");
        if (produit.getPrixAchat() == 0)
            throw new ProduitGratuitException("Le produit " + produit.getNom() + " est gratuit");
        return produit;
    }

    //ajouter un produit
    @ApiOperation(value = "Ajoute un produit")
    @PostMapping(value = "/Produits")
    public ResponseEntity<Void> ajouterProduit(@Valid @RequestBody Product product) {
        Product productAdded = productDao.save(product);
        if (productAdded == null)
            return ResponseEntity.noContent().build();
        URI location = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(productAdded.getId())
                .toUri();
        return ResponseEntity.created(location).build();
    }

    //Récupérer un produit par son Id
    @ApiOperation(value = "Supprime un produit grâce à son ID à condition que celui-ci soit en stock!")
    @DeleteMapping(value = "/Produits/{id}")
    public void supprimerProduit(@PathVariable int id) {
        productDao.delete(id);
    }

    //Met à jour un produit
    @ApiOperation(value = "Met à jour un produit")
    @PutMapping(value = "/Produits")
    public void updateProduit(@RequestBody Product product) {
        productDao.save(product);
    }

    //Pour les tests
    @ApiOperation(value = "Pour les tests")
    @GetMapping(value = "test/produits/{prix}")
    public List<Product> testeDeRequetes(@PathVariable int prix) {
        return productDao.chercherUnProduitCher(400);
    }

    //Affiche les produits avec la marge
    @ApiOperation(value = "Affiche les produits avec la marge")
    @GetMapping(value = "/margeProduits")
    private ArrayList<String> calculerMargeProduit() {
        Iterable<Product> produits = productDao.findAll();
        ArrayList<String> listProduits = new ArrayList<>();
        int marge;
        for (Product produit : produits) {
            marge = produit.getPrix() - produit.getPrixAchat();
            listProduits.add(produit.toString() + ":" + marge);
        }
        return listProduits;
    }

    //Tri aplpha
    @ApiOperation(value = "Affiche les produits dans l'ordre alphabétique")
    @GetMapping(value = "produits/tri")
    public List<Product> triProduit() {
        return productDao.triProduct();
    }
}
