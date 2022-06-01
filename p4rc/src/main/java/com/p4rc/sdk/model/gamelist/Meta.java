
package com.p4rc.sdk.model.gamelist;

import org.json.JSONObject;

public class Meta {

    private Integer totalFeatured;
    private Integer total;

    public Integer getTotalFeatured() {
        return totalFeatured;
    }

    public void setTotalFeatured(Integer totalFeatured) {
        this.totalFeatured = totalFeatured;
    }

    public Integer getTotal() {
        return total;
    }

    public void setTotal(Integer total) {
        this.total = total;
    }


    public static Meta fromJSON(JSONObject json) {
        if (json == null) {
            return null;
        }
        Meta meta = new Meta();
        meta.setTotalFeatured(json.optInt("totalFeatured"));
        meta.setTotal(json.optInt("total"));
        return meta;
    }


    @Override
    public String toString() {
        return "Meta{" +
                "totalFeatured=" + totalFeatured +
                ", total=" + total +
                '}';
    }
}
