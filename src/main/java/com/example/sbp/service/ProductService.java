package com.example.sbp.service;

import com.example.sbp.common.exception.BadRequestException;
import com.example.sbp.domain.Product;
import com.example.sbp.repository.ProductRepository;
import com.example.sbp.vo.ProductVo;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static com.example.sbp.common.support.Status.PRODUCT_NOT_FOUND;

@Service
@RequiredArgsConstructor
public class ProductService {
    private final ProductRepository productRepository;

    @Transactional(readOnly = true)
    public ProductVo findById(Long id) {
        var productEntity = productRepository.findById(id)
            .orElseThrow(() -> new BadRequestException(PRODUCT_NOT_FOUND));

        return ProductVo.from(productEntity);
    }

    @Transactional
    public void create(CreateRequest request) {
        var product = request.toEntity();

        productRepository.save(product);
    }

    public record CreateRequest(
        String name
    ) {
        public Product toEntity() {
            return new Product(name);
        }
    }
}
