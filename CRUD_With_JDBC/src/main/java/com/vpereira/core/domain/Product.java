package com.vpereira.core.domain;

import com.vpereira.annotation.Column;
import com.vpereira.annotation.Id;
import com.vpereira.annotation.Table;

import java.util.Objects;

@Table("Product")
public class Product implements Entity{

    @Id
    private Long code;
    @Column(value = "product_name", notNull = true, length = 20)
    private String name;
    @Column(value = "price", notNull = true)
    private Float price;
    @Column(value = "product_desc", notNull = true)
    private String desc;

    @Override
    public Long getId() {
        return code;
    }

    @Override
    public void setId(Long id) {
        this.code = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Float getPrice() {
        return price;
    }

    public void setPrice(Float price) {
        this.price = price;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Product product = (Product) o;
        return Objects.equals(code, product.code) && Objects.equals(name, product.name) && Objects.equals(price, product.price) && Objects.equals(desc, product.desc);
    }

    @Override
    public int hashCode() {
        return Objects.hash(code, name, price, desc);
    }

    @Override
    public String toString() {
        return "Product{" +
                "code=" + code +
                ", name='" + name + '\'' +
                ", price=" + price +
                ", desc='" + desc + '\'' +
                '}';
    }
}
