package com.nnk.springboot.domain;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;


@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "trade")
public class Trade {
    // TODO: Map columns in data table TRADE with corresponding java fields
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "trade_id")
    Integer tradeId;
    String account;
    String type;

    @Column(name = "buy_quantity")
    Double buyQuantity;

    @Column(name = "sell_quantity")
    Double sellQuantity;

    @Column(name = "buy_price")
    Double buyPrice;

    @Column(name = "sell_price")
    Double sellPrice;

    String benchmark;

    @Column(name = "trade_date")
    Timestamp tradeDate;
    String security;
    String status;
    String trader;
    String book;

    @Column(name = "creation_name")
    String creationName;

    @Column(name = "creation_date")
    Timestamp creationDate;

    @Column(name = "revision_name")
    String revisionName;

    @Column(name = "revision_date")
    Timestamp revisionDate;

    @Column(name = "deal_name")
    String dealName;

    @Column(name = "deal_type")
    String dealType;

    @Column(name = "source_list_id")
    String sourceListId;
    String side;

}
