package com.bugnbass.backend.model;
import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.Data;
import java.util.UUID;
import lombok.*;

@Entity
@Table (name = "images")
@Data
@NoArgsConstructor
public class Image {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name= "image_id", updatable = false, nullable = false)
    private UUID imageId;

    @Column(nullable = false)
    private String url;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id")
    @JsonBackReference
    @Setter
    private Product product;
}
