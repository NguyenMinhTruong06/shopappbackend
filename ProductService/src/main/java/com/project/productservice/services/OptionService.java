package com.project.productservice.services;

import com.project.productservice.dtos.OptionDTO;
import com.project.productservice.excepions.DataNotFoundException;
import com.project.productservice.models.Option;
import com.project.productservice.models.Product;
import com.project.productservice.repositories.ProductOptionRepository;
import com.project.productservice.repositories.ProductRepository;
import lombok.*;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class OptionService implements IOptionService{
    private final ProductRepository productRepository;
    private final ProductOptionRepository productOptionRepository;



    @Override
    public Option createOption(Long productId, OptionDTO optionDTO) throws DataNotFoundException {
        Product existingProduct = productRepository.findById(productId)
                .orElseThrow(()->new DataNotFoundException("Cannot find product whith id: "+ optionDTO.getProductId()));
        Option newProductOption = Option.builder()
                .product(existingProduct)
                .option(optionDTO.getOption())
                .price(optionDTO.getPrice())
                .build();
        return productOptionRepository.save(newProductOption);
    }

    @Override
    public List<OptionDTO> getOptionByProductId(Long productId) {
        List<Option> options = productOptionRepository.findByProductId(productId);
        List<OptionDTO> optionDTOList = new ArrayList<>();

        for (Option option : options) {
            OptionDTO optionDTO = new OptionDTO();
            optionDTO.setId(option.getId());
            optionDTO.setOption(option.getOption());
            optionDTO.setProductId(productId);
            optionDTO.setPrice(option.getPrice());
            optionDTOList.add(optionDTO);
        }

        return optionDTOList;
    }

    @Override
    public void deleteProductOption(long id) {

            Optional<Option> optionalProductOption = productOptionRepository.findById(id);
            optionalProductOption.ifPresent(productOptionRepository::delete);


    }

    @Override
    public Option updateOption(Long productId,Long id, OptionDTO optionDTO) throws DataNotFoundException {
       Optional<Product> existingProductOpt= productRepository.findById(productId);
       if (!existingProductOpt.isPresent()){
           throw new DataNotFoundException("Không tìm thấy productId: "+productId);
       }
        Product existingProduct = existingProductOpt.get();
        Optional<Option> optionalExistingOption = productOptionRepository.findById(id);
        if (!optionalExistingOption.isPresent()) {
            throw new DataNotFoundException("Option not found for ID: " + id);
        }
        Option existingOption = optionalExistingOption.get();
        existingOption.setOption(optionDTO.getOption());
        existingOption.setPrice(optionDTO.getPrice());
        existingOption.setProduct(existingProduct);
        return productOptionRepository.save(existingOption);
    }


}
