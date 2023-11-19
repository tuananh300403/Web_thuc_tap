package com.poly.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.List;

@Entity
@Table(name = "bill_product")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class BillProduct {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne
    @JoinColumn(name = "id_product")
    private ProductDetail product;

    @ManyToOne
    @JoinColumn(name = "id_bill")
    private Bill bill;

    @Column(name="quantity")
    private Integer quantity;

    @Column(name="price")
    private BigDecimal price;

    @Column(name="reduced_money")
    private BigDecimal reducedMoney;

    @Column(name="note")
    private String note;

    @Column(name="reason")
    private String reason;

    @Column(name="quantity_request_return")
    private Integer quantityRequestReturn;

    @Column(name="quantity_accept_return")
    private Integer quantityAcceptReturn;

    @Column(name="status")
    private Integer status;

    @OneToMany(mappedBy="billProduct")
    private List<ImageReturned> listImage;
}
