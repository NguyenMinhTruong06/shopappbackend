package com.project.productservice.services;

import com.cloudinary.Cloudinary;
import com.project.productservice.dtos.ProductDTO;
import com.project.productservice.dtos.ProductImageDTO;
import com.project.productservice.excepions.DataNotFoundException;
import com.project.productservice.excepions.InvalidParamException;
import com.project.productservice.models.Category;
import com.project.productservice.models.Product;
import com.project.productservice.models.ProductImage;
import com.project.productservice.repositories.CategoryRepository;
import com.project.productservice.repositories.ProductImageRepository;
import com.project.productservice.repositories.ProductRepository;

import com.project.productservice.responses.ProductResponse;
import com.project.userservice.exception.ValidationException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.*;

@RequiredArgsConstructor
@Service
public class ProductService implements IProductService{
    private final Cloudinary cloudinary;

    private final ProductImageRepository productImageRepository;
    private final ProductRepository productRepository;
    private final CategoryRepository categoryRepository;
    @Override
    public Product createProduct(ProductDTO productDTO) throws DataNotFoundException {
        Category existingCategory = categoryRepository
                .findById(productDTO.getCategoryId())
                .orElseThrow(()-> new DataNotFoundException("cannot find category with id "
                        + productDTO.getCategoryId()));
        Product newProduct = Product.builder()
                .name(productDTO.getName())
                .price(productDTO.getPrice())
                .thumbnail(productDTO.getThumbnail())
                .description(productDTO.getDescription())
                .category(existingCategory)
                .build();
        return productRepository.save(newProduct);
    }

    @Override
    public Product getProductById(long productId) throws DataNotFoundException {

        return productRepository.findById(productId)
                .orElseThrow(()->new DataNotFoundException("cannot find product with id"+ productId));

    }

    @Override
    public Page<ProductResponse> getAllProducts(String keyword ,Long categoryId,PageRequest pageRequest) {
        Page<Product> productPage;
        productPage = productRepository.searchProducts(categoryId,keyword,pageRequest);

        return productPage.map(ProductResponse::fromProduct);


    }

    @Override
    public Product updateProduct(long id, ProductDTO productDTO) throws DataNotFoundException {
        Product existingProduct = getProductById(id);
        if(existingProduct != null){
            Category existingCategory = categoryRepository
                    .findById(productDTO.getCategoryId())
                            .orElseThrow(()-> new DataNotFoundException("Cannot find category with id "+productDTO.getCategoryId()));
            existingProduct.setName(productDTO.getName());
            existingProduct.setCategory(existingCategory);
            existingProduct.setDescription(productDTO.getDescription());
            existingProduct.setPrice(productDTO.getPrice());
            existingProduct.setThumbnail(productDTO.getThumbnail());
            return productRepository.save(existingProduct);
        }
        return null;
    }

    @Override
    public void deleteProduct(long id) {
        Optional<Product> optionalProduct = productRepository.findById(id);
        optionalProduct.ifPresent(productRepository::delete);

    }

    @Override
    public boolean existsByName(String name) {

        return productRepository.existsByName(name);
    }
    @Override
    public ProductImage createProductImage (Long productId, ProductImageDTO productImageDTO) throws Exception {
    Product existingProduct = productRepository
            .findById(productId)
            .orElseThrow(()->new DataNotFoundException("Cannot find product whith id: "+ productImageDTO.getProductId()));
            ProductImage newProductImage = ProductImage.builder()
                    .product(existingProduct)
                    .imageUrl(productImageDTO.getImageUrl())
                    .build();
            int size = productImageRepository.findByProductId(productId).size();
            if(size >= ProductImage.MAXIMUM_IMAGES_FER_PRODUCT){
                throw new InvalidParamException("Number of image <="+ProductImage.MAXIMUM_IMAGES_FER_PRODUCT);
            }
            return productImageRepository.save(newProductImage);
    }

    @Override

    public List<String> getImageNameByProductId(Long productId) {

        List<ProductImage> productImages = productImageRepository.findByProductId(productId);
        List<String> imageUrls = new ArrayList<>();

        for (ProductImage productImage : productImages) {
            imageUrls.add(productImage.getImageUrl());
        }

        return imageUrls;
    }


    public String uploadFile(MultipartFile multipartFile, String folderName) throws IOException {
//        MultilpartFile: cung cấp các phương thức để có thể truy cập dữ liệu của tệp trực tiếp
//        thông qua InputStream và lấy thông tin về tên tệp và kiểu MIME
//        kiểu MIME: chuỗi ký tự dùng để định danh loại nội dung của một tệp hoặc dữ liệu,
//        giúp máy tính và trình duyệt hiểu cách xử lý dữ liệu cụ thể và cung cấp cho máy chủ
//        và máy khách thông tin về loại dữ liệu đang truyền qua mạng
        Map<String, Object> uploadParams = new HashMap<>();
        uploadParams.put("public_id", UUID.randomUUID().toString());
        uploadParams.put("folder", folderName);

        return cloudinary.uploader()
                .upload(multipartFile.getBytes(), uploadParams)  //chuyển đổi tệp đa phương tiện thành mảng byte sau đó upload
                .get("url")                                     //truy xuất URL của tệp
                .toString();
    }
}
