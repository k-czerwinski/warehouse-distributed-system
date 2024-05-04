package com.bd2.warehousedistributedsystem.model;

import lombok.RequiredArgsConstructor;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

@RequiredArgsConstructor
public class Cart {
    private final Map<Product, Integer> products;

    public static Map<Long, Integer> getProductCodesListFromCookie(String productCodesString) {
        if (productCodesString.isBlank()) {
            return new HashMap<>();
        }
        return Arrays.stream(productCodesString.split("-"))
                .map(productCode -> {
                    String [] num = productCode
                            .split("\\.");
                    return Map.entry(Long.valueOf(num[0]), Integer.valueOf(num[1]));
                }).collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
    }

    public static String toProductCodesString(Map<Long,Integer> productCodes) {
        if (productCodes.isEmpty()) {
            return "";
        }
        StringBuilder builder = new StringBuilder("");
        for (var entry : productCodes.entrySet()) {
            builder.append("-%d.%d".formatted(entry.getKey(), entry.getValue()));
        }
        return builder.toString().substring(1);
    }
    public Map<Product, Integer> getProductsInCart() {
        return products;
    }
}
