package com.example.petel.entity;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "PETEL_ORDER_ITEMS")
public class OrderItemsEntity {

    /**
     * Table ID
     */
    @Id
    @Column(name = "ID", nullable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * 訂單編號
     */
    @Column(name = "ORDER_ID", nullable = false)
    private Long orderId;

    /**
     * 入住日期
     */
    @Column(name = "ARRIVAL_DATE", nullable = false)
    private String arrivalDate;

    /**
     * 房型編號
     */
    @Column(name = "ROOM_ID", nullable = false)
    private Long roomId;

    /**
     * 訂單對應到該房型編號的數量
     */
    @Column(name = "QUANTITY", nullable = false)
    private Integer quantity;

    /**
     * 該房型價格
     */
    @Column(name = "PRICE", nullable = false)
    private Integer price;
}