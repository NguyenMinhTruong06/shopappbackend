package com.project.productservice.controller;

import com.github.javafaker.Faker;
import com.project.productservice.dtos.ProductDTO;
import com.project.productservice.dtos.ProductImageDTO;
import com.project.productservice.excepions.DataNotFoundException;
import com.project.productservice.models.Product;
import com.project.productservice.models.ProductImage;
import com.project.productservice.responses.ProductListResponse;
import com.project.productservice.responses.ProductResponse;
import com.project.productservice.services.IProductService;

import com.project.productservice.services.ProductService;
import com.project.userservice.exception.ValidationException;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.UrlResource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.util.StringUtils;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

@RestController
@RequestMapping("api/v1/products")
@RequiredArgsConstructor
public class ProductController {
    private final ProductService productService;

    @GetMapping("/test")
    public ResponseEntity<?> Test(){
        return ResponseEntity.ok("sdfdsf");
    }



    @GetMapping("/all")
    public ResponseEntity<ProductListResponse> getProducts(
            @RequestParam(defaultValue = "") String keyword,
            @RequestParam(defaultValue = "0", name ="category_id") Long categoryId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int limit
    ) {
        PageRequest pageRequest = PageRequest.of(page, limit, Sort.by("createdAt").descending());
        Page<ProductResponse> productPage = productService.getAllProducts( keyword , categoryId, pageRequest);
        int totalPages = productPage.getTotalPages();
        List<ProductResponse> products = productPage.getContent();

        return ResponseEntity.ok(ProductListResponse.builder()
                .products(products)
                .totalPages(totalPages)
                .build());
    }

    @GetMapping("/img/{productId}")
    public ResponseEntity<?> viewImg(@PathVariable Long productId) {
        try {

            String imageName = productService.getImageNameByProductId(productId).get(0).toString();

            if(imageName != null) {
                java.nio.file.Path imagePath = Paths.get("uploads/" + imageName);
                UrlResource resource = new UrlResource(imagePath.toUri());

                if(resource.exists()){
                    return ResponseEntity.ok()
                            .contentType(MediaType.IMAGE_JPEG)
                            .body(resource);
                } else {
                    return ResponseEntity.notFound().build();
                }
            } else {
                return ResponseEntity.notFound().build(); // If image name is not found for the given product ID
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Internal Server Error");
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getProductById(@PathVariable("id") Long productId) {
        try {
            Product existingProduct = productService.getProductById(productId);
            return ResponseEntity.ok(ProductResponse.fromProduct(existingProduct));
        } catch (DataNotFoundException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }

    }

    @PostMapping("/post")
    public ResponseEntity<?> createProduct(@Valid @RequestBody
                                           ProductDTO productDTO,

                                           BindingResult result) {

        try {
            if (result.hasErrors()) {
                List<String> errorMessages = result.getFieldErrors().stream()
                        .map(FieldError::getDefaultMessage)
                        .toList();
                return ResponseEntity.badRequest().body(errorMessages);
            }
            Product newProduct = productService.createProduct(productDTO);
            return ResponseEntity.ok(newProduct);

        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PostMapping(value = "uploads/{id}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<?> uploadImages(@PathVariable("id") Long productId,
                                          @ModelAttribute("files") List<MultipartFile> files) throws  Exception{
        try {
            Product existingProduct = productService.getProductById(productId);
            files = files == null ? new ArrayList<MultipartFile>() : files;
            if (files.size() > ProductImage.MAXIMUM_IMAGES_FER_PRODUCT) {
                return ResponseEntity.badRequest().body("Chỉ được upload max 5 img");
            }
            List<ProductImage> productImages = new ArrayList<>();
            for (MultipartFile file : files) {
                if (file.getSize() == 0) {
                    continue;
                }
                if (file.getSize() > 10 * 2024 * 2024) {

                    return ResponseEntity.status(HttpStatus.PAYLOAD_TOO_LARGE)
                            .body("File is too large: Maximum size is 10mb");
                }
                String contentType = file.getContentType();
                if (contentType == null || !contentType.startsWith("image/")) {
                    return ResponseEntity.status(HttpStatus.UNSUPPORTED_MEDIA_TYPE).body("file must be an image");
                }
                String imageURL = productService.uploadFile(file, "product");

                ProductImage productImage = productService.createProductImage(
                        existingProduct.getId(),
                        ProductImageDTO.builder()
                                .imageUrl(imageURL.replace("http", "https"))
                                .build()
                );
                productImages.add(productImage);
            }
            return ResponseEntity.ok().body(productImages);
        } catch (DataNotFoundException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
//    @PutMapping(value = "uploads/{id}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
//    public ResponseEntity<?> updateImages(@PathVariable("id") Long productId,
//                                          @ModelAttribute("files") List<MultipartFile> files) throws Exception {
//        try {
//            Product existingProduct = productService.getProductById(productId);
//            files = files == null ? new ArrayList<>() : files;
//            if (files.size() > ProductImage.MAXIMUM_IMAGES_FER_PRODUCT) {
//                return ResponseEntity.badRequest().body("Chỉ được upload max 5 img");
//            }
//            productService.deleteProductImagesByProductId(productId);
//            List<ProductImage> productImages = new ArrayList<>();
//            for (MultipartFile file : files) {
//                if (file.getSize() == 0) continue;
//                if (file.getSize() > 10 * 1024 * 1024) {
//                    return ResponseEntity.status(HttpStatus.PAYLOAD_TOO_LARGE)
//                            .body("File is too large: Maximum size is 10mb");
//                }
//                String contentType = file.getContentType();
//                if (contentType == null || !contentType.startsWith("image/")) {
//                    return ResponseEntity.status(HttpStatus.UNSUPPORTED_MEDIA_TYPE).body("file must be an image");
//                }
//                String imageURL = productService.uploadFile(file, "product");
//                ProductImage productImage = productService.createProductImage(
//                        existingProduct.getId(),
//                        ProductImageDTO.builder()
//                                .imageUrl(imageURL.replace("http", "https"))
//                                .build()
//                );
//                productImages.add(productImage);
//            }
//            return ResponseEntity.ok().body(productImages);
//        } catch (DataNotFoundException e) {
//            return ResponseEntity.badRequest().body(e.getMessage());
//        }
//    }



    private boolean isImageFile(MultipartFile file) {
        String contentType = file.getContentType();
        return contentType != null && contentType.startsWith("image/");
    }


    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteProduct(@PathVariable long id) {
        try {
            productService.deleteProduct(id);
            return ResponseEntity.ok(String.format("Product with id =%d delete successfully", id));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }

    }

    @PostMapping("/generateFakeProducts")
    private ResponseEntity<String> generateFakeProducts() {
        Faker faker = new Faker();
        for (int i = 0; i < 100; i++) {
            String productName = faker.commerce().productName();
            if (productService.existsByName(productName)) {
                continue;
            }
            ProductDTO productDTO = ProductDTO.builder()
                    .name(productName)
//                    .price((float) faker.number().numberBetween(10, 9000000))
                    .thumbnail("")
                    .description(faker.lorem().sentence())
                    .categoryId((long) faker.number().numberBetween(3, 5))
                    .build();
            try {
                productService.createProduct(productDTO);
            } catch (DataNotFoundException e) {
                return ResponseEntity.badRequest().body(e.getMessage());
            }
        }
        return ResponseEntity.ok("Fake Product successfully");
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateProduct(
            @PathVariable long id,
            @RequestBody ProductDTO productDTO
    ) {
        try {
            Product updateProduct = productService.updateProduct(id, productDTO);
            return ResponseEntity.ok(updateProduct);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }
    @DeleteMapping ("image/delete/{id}")
    public ResponseEntity<String> deleteImage(@PathVariable long id){
        productService.deleteProductImagesById(id);
        return ResponseEntity.ok("Xoá thàng công id "+id);
    }
}
