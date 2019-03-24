package com.my.test.demo.mongoentity;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * 用户订单数据（mongodb存储）
 */
@Data
@Document(collection = "order")
public class Order implements Serializable {

    /**
     * 主键
     */
    @Id
    private Long id;

    /**
     * 用户ID
     */
    @Indexed
    private Long userId;

    /**
     * 商品名称
     */
    private String productName;

    /**
     * 订单编号
     */
    private String orderNo;

    /**
     * 订单金额
     */
    private BigDecimal amount;

    /**
     * 订单状态
     */
    private Integer state;
}
