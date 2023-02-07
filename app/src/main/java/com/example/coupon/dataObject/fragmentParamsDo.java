package com.example.coupon.dataObject;

public class fragmentParamsDo {
    private int widthPixel = 0;

    private int heightPixel = 0;

    private float density = 0;

    private int pageNo = 1;

    private String sort = "";

    private String sortType = "";

    private Boolean hasCoupon = false;

    private String activityType = "ele";

    public void setWidthPixel(int widthPixel) {
        this.widthPixel = widthPixel;
    }
    public int getWidthPixel() {
        return this.widthPixel;
    }

    public void setHeightPixel(int heightPixel) {
        this.heightPixel = heightPixel;
    }
    public int getHeightPixel() {
        return heightPixel;
    }

    public void setDensity(float density) {
        this.density = density;
    }

    public float getDensity() {
        return this.density;
    }

    public int getPageNo() {
        return pageNo;
    }

    public void setPageNo(int pageNo) {
        this.pageNo = pageNo;
    }

    public String getSort() {
        return sort;
    }

    public void setSort(String sort) {
        this.sort = sort;
    }

    public String getSortType() {
        return sortType;
    }

    public void setSortType(String sortType) {
        this.sortType = sortType;
    }

    public Boolean getHasCoupon() {
        return hasCoupon;
    }

    public void setHasCoupon(Boolean hasCoupon) {
        this.hasCoupon = hasCoupon;
    }

    public String getActivityType() {
        return activityType;
    }

    public void setActivityType(String activityType) {
        this.activityType = activityType;
    }
}
