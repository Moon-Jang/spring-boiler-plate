package com.example.sbp.controller;

import com.example.sbp.common.web.ApiResponse;
import com.example.sbp.service.ProductService;
import com.example.sbp.vo.ProductVo;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/v1/products")
@RequiredArgsConstructor
public class ProductController {
    private final ProductService productService;

    @GetMapping("/{productId}")
    public ApiResponse<ProductVo> findById(@PathVariable Long productId) {
        return ApiResponse.success(
            productService.findById(productId)
        );
    }
}
