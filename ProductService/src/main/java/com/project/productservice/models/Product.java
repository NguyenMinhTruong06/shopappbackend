package com.project.productservice.models;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.web.multipart.MultipartFile;
import com.project.productservice.models.ProductImage;

import java.util.List;


@Entity
@Table(name = "products")
@Setter
@Data
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Product extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name", nullable = false, length = 255)
    private String name;
    //     private Float price;
    @Column(name = "thumbnail", nullable = true, length = 255)
    private String thumbnail;
    @Column(name = "description")
    private String description;


    @ManyToOne
    @JoinColumn(name = "category_id")
    private Category category;

    @OneToMany(mappedBy = "product", fetch = FetchType.LAZY) // Lazy loading for performance
    @JsonManagedReference
    private List<ProductImage> images;

    @OneToMany(mappedBy = "product", fetch = FetchType.LAZY) // Lazy loading for performance
    @JsonManagedReference
    private List<Option> options;



}
