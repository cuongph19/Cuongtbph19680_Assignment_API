package com.example.cuongtbph19680_assignment_api;

public class Product {

    private String _id;
    private String name;
    private double price;
    private int quantity;
    private boolean inventory;
    private String image;

    public Product() {
        this._id = _id;
        this.name = name;
        this.price = price;
        this.quantity = quantity;
        this.inventory = inventory;
        this.image = image;
    }

    public String get_id() {
        return _id;
    }

    public void set_id(String _id) {
        this._id = _id;
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

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public boolean isInventory() {
        return inventory;
    }

    public void setInventory(boolean inventory) {
        this.inventory = inventory;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    @Override
    public String toString() {
        return "Product{" +
                "_id='" + _id + '\'' +
                ", name='" + name + '\'' +
                ", price=" + price +
                ", quantity=" + quantity +
                ", inventory=" + inventory +
                ", image='" + image + '\'' +
                '}';
    }
}