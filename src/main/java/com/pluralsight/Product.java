package com.pluralsight;

public class Product {
    private String SKU;
    private String name;
    private double price;
    private String department;

    public Product(String SKU, String name, double price, String department) {
        this.SKU = SKU;
        this.name = name;
        this.price = price;
        this.department = department;
    }

    public Product() {
    }

    public String getSKU() {
        return SKU;
    }

    public void setSKU(String SKU) {
        this.SKU = SKU;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public String getDepartment() {
        return department;
    }

    public void setDepartment(String department) {
        this.department = department;
    }

}
