package com.vpereira.core.domain;

import com.vpereira.annotation.FieldMongoDB;
import com.vpereira.annotation.Id;
import com.vpereira.annotation.DocumentMongoDB;

import java.util.Objects;

@DocumentMongoDB("Product")
public class Product implements Entity{

    @Id
    private String code;
    @FieldMongoDB(value = "product_name")
    private String name;
    @FieldMongoDB(value = "price")
    private Float price;
    @FieldMongoDB(value = "product_desc")
    private String desc;

    @Override
    public String getId() {
        return code;
    }

    @Override
    public void setId(String id) {
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
