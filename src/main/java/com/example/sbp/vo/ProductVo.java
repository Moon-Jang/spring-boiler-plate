package com.example.sbp.vo;

import com.example.sbp.domain.Product;

public record ProductVo(
  Long id,
  String name
){
    public static ProductVo from(Product product) {
        return new ProductVo(
            product.id(),
            product.name()
        );
    }
}
