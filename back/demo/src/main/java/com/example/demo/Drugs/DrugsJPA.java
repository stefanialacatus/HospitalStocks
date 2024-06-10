//package com.example.demo.Drugs;
//
//import javax.persistence.*;
//import java.math.BigDecimal;
//
//
//@Entity
//@Table(name = "drugs")
//public class DrugsJPA {
//    @Id
//    @GeneratedValue(strategy = GenerationType.IDENTITY)
//    @Column(name = "id")
//    private Integer id;
//
//    @Column(name = "name")
//    private String name;
//    @Column(name = "dosage_form")
//    private String dosageForm;
//    @Column(name= "unit")
//    private String unit;
//    @Column(name= "price")
//    private BigDecimal price;
//    @Column(name= "stock")
//    private int stock;
//    public Integer getId() {
//        return id;
//    }
//
//    public void setId(Integer id) {
//        this.id = id;
//    }
//    public String getName(String name) {
//        return name;
//    }
//
//    public void setName(String name) {
//        this.name = name;
//    }
//}
