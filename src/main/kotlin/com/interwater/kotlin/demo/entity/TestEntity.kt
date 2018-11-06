package com.interwater.kotlin.demo.entity

import com.fasterxml.jackson.annotation.JsonFormat
import java.math.BigDecimal
import java.util.*
import javax.persistence.*

@Entity
@Table(name = "test")
data class TestEntity (@Column(name = "base")
                      private var base: String? = null) {
    constructor() : this(null)

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id")
    private var id: Long = 0

    @Column(name = "counter")
    private var counter: String? = null

    @Column(name = "price")
    private var price: BigDecimal? = null

    @Column(name = "order_price")
    private var orderPrice: BigDecimal? = null

    @Column(name = "sell_price")
    private var sellPrice: BigDecimal? = null

    @Column(name = "sell_order_price")
    private var sellOrderPrice: BigDecimal? = null

    @Column(name = "amount")
    private var amount: BigDecimal? = null

    @Column(name = "strategy_id")
    private var strategyId: Long = 0

    @Column(name = "strategy_name")
    private var strategyName: String? = null

    @Column(name = "sell_strategy_name")
    private var sellStrategyName: String? = null

    @Column(name = "is_finished")
    private var isFinished: Long? = null

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "Asia/Seoul")
    @Column(name = "created_time", updatable = false, insertable = false)
    private var createdTime: Date? = null

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "Asia/Seoul")
    @Column(name = "updated_time", insertable = false)
    private var updatedTime: Date? = null
}