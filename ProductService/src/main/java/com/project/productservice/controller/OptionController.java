package com.project.productservice.controller;

import com.project.productservice.dtos.OptionDTO;
import com.project.productservice.excepions.DataNotFoundException;
import com.project.productservice.models.Option;
import com.project.productservice.models.Product;
import com.project.productservice.services.IOptionService;
import com.project.productservice.services.OptionService;
import com.project.productservice.services.ProductService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("api/v1/options")
@RequiredArgsConstructor
public class OptionController {

    private final OptionService optionService;
    private final ProductService productService;

    @PostMapping("/post/{productId}")
    public ResponseEntity<?> creatProductOption(@PathVariable Long productId, @Valid @RequestBody List<OptionDTO> optionDTOs, BindingResult result) {
        try {
            Product existingProduct = productService.getProductById(productId);
            if (existingProduct == null) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Product not found");
            }
            List<Option> createdOptions = new ArrayList<>();
            for (OptionDTO optionDTO : optionDTOs) {
                Option newOption = optionService.createOption(
                        existingProduct.getId(),
                        OptionDTO.builder()
                                .option(optionDTO.getOption())
                                .price(optionDTO.getPrice())
                                .build());
                createdOptions.add(newOption);
            }
            return ResponseEntity.ok().body(createdOptions);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }

    }

        @GetMapping("/get/{productId}")
    public ResponseEntity<?> getProductOption(@PathVariable Long productId) {
        try {
            Product existingProduct = productService.getProductById(productId);
            if (existingProduct == null) {
                throw new DataNotFoundException("Product not found for ID: " + productId);
            }

            List<OptionDTO> options = optionService.getOptionByProductId(productId);
            if (options.isEmpty()) {
                throw new DataNotFoundException("No options found for Product ID: " + productId);
            }

            return ResponseEntity.ok(options);
        } catch (DataNotFoundException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }



    }
    @DeleteMapping("delete/{id}")
    public ResponseEntity<?> deleteOption(@PathVariable Long id){
        try{
        optionService.deleteProductOption(id);
        return ResponseEntity.ok(String.format("xoá thành công id: "+id));
        }catch (Exception e){
           return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
    @PutMapping("put/{productId}/{id}")
    public ResponseEntity<?> updateOption(@PathVariable Long productId,@PathVariable Long id, @RequestBody OptionDTO optionDTO) throws DataNotFoundException {
        Option updateOption = optionService.updateOption(productId,id,optionDTO);
        return ResponseEntity.ok().body(updateOption);
    }

}