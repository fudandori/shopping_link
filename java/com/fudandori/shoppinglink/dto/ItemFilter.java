package com.fudandori.shoppinglink.dto;

public class ItemFilter {
    private boolean name;
    private boolean brand;
    private String nameValue;
    private String brandValue;
    private String cat;

    public void reset() {
        name = false;
        brand = false;
        nameValue = null;
        brandValue = null;
        cat = null;
    }

    public boolean isName() {
        return name;
    }

    public void setName(boolean name) {
        this.name = name;
    }

    public boolean isBrand() {
        return brand;
    }

    public void setBrand(boolean brand) {
        this.brand = brand;
    }

    public String getNameValue() {
        return nameValue;
    }

    public void setNameValue(String nameValue) {
        this.nameValue = nameValue;
    }

    public String getBrandValue() {
        return brandValue;
    }

    public void setBrandValue(String brandValue) {
        this.brandValue = brandValue;
    }

    public String getCat() {
        return cat;
    }

    public void setCat(String cat) {
        this.cat = cat;
    }
}
