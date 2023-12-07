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
@Table(name = "bidlist")
public class BidList {
    // TODO: Map columns in data table BIDLIST with corresponding java fields
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "bid_list_id")
    Integer BidListId;
    String account;
    String type;
    @Column(name = "bid_quantity")
    Double bidQuantity;
    @Column(name = "ask_quantity")
    Double askQuantity;
    Double bid;
    Double ask;
    String benchmark;
    @Column(name = "bid_list_date")
    Timestamp bidListDate;
    String commentary;
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
