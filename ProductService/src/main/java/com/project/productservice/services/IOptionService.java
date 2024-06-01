package com.project.productservice.services;

import com.project.productservice.dtos.OptionDTO;
import com.project.productservice.excepions.DataNotFoundException;
import com.project.productservice.models.Option;

import java.util.List;

public interface IOptionService {
    Option createOption (Long productId, OptionDTO optionDTO) throws DataNotFoundException;
    List<OptionDTO> getOptionByProductId(Long productId);

    void deleteProductOption(long Id);

    Option updateOption (Long productId,Long id, OptionDTO optionDTO) throws DataNotFoundException;
}
