package com.bugnbass.backend.service;

import com.bugnbass.backend.model.Product;
import com.bugnbass.backend.model.enums.ProductCategory;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class UserProductServiceTests {

    List<Product> mockData = List.of(
            new Product("Newerton Pro", ProductCategory.PIANOS, "Professional studio piano with weighted keys and authentic grand piano sound.", 99900, 0, "Newerton", true, 5, false),  // inactive
            new Product("PrestoTune White-F", ProductCategory.PIANOS, "Elegant white finish piano with advanced soundboard technology for superior acoustics.", 350000, 0, "PrestoTune Pianos", true, 5, true), // active, stock true
            new Product("ChordStone", ProductCategory.GUITARS, "Solid-body guitar with stone inlays and sustain-enhancing design.", 88900, 500, "ToneRiver", false, 5, false), // inactive and stock false
            new Product("StringGale", ProductCategory.GUITARS, "Professional concert guitar with powerful projection and clarity.", 57900, 900, "StringSpirit", true, 7, true), // active and stock true
            new Product("StrumEcho V2", ProductCategory.PIANOS,"Improved version of our popular model with better bow response.", 88000, 1600, "StrumVista Guitars", false, 7, false), // inactive and stock false
            new Product("VirtuosoTune", ProductCategory.VIOLINS, "Concert-quality violin with hand-carved spruce top and maple back.", 64500, 1100, "Virtuoso Makers", true, 2, true) // active and stock true
    );


}
