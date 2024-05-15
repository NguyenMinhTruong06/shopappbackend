package com.project.productservice.services;

import com.project.productservice.dtos.ProductDTO;
import com.project.productservice.dtos.ProductImageDTO;
import com.project.productservice.excepions.DataNotFoundException;
import com.project.productservice.models.Product;
import com.project.productservice.models.ProductImage;
import com.project.productservice.responses.ProductResponse;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@Service
public interface IProductService {
    public Product createProduct(ProductDTO productDTO) throws DataNotFoundException;

    Product getProductById(long Id) throws DataNotFoundException;

    Page<ProductResponse> getAllProducts(String keyword ,Long categoryId,PageRequest pageRequest);

    Product updateProduct(long id, ProductDTO productDTO) throws DataNotFoundException;

    void deleteProduct(long Id);

    boolean existsByName (String name);
    ProductImage createProductImage (Long productId, ProductImageDTO productImageDTO) throws Exception;
    List<String> getImageNameByProductId(Long productId);
    String uploadFile(MultipartFile multipartFile, String folderName) throws IOException;
}
