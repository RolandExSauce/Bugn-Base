package com.bugnbass.backend.model;
import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import java.util.UUID;


@Entity
@Table (name = "images")
@Getter
public class Image {

    @Id
    @Column(name= "image_id", updatable = false)
    private String imageId = UUID.randomUUID().toString();

    @Setter
    private String url;

    @ManyToOne
    @JoinColumn(name = "product_id")
    @JsonBackReference
    @Setter
    private Product product;
};
