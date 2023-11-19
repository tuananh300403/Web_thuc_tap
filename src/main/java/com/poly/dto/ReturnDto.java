package com.poly.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ReturnDto {
    private Integer idBillProduct;
    private String nameProduct;
    private String quantityReturn;
    private String reason;
    private List<ImageReturnDto> image;
}
