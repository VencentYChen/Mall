package com.wangku.demo.mall;

import java.util.List;

/**
 * Created by Administrator on 2017/8/2 0002.
 */

public class Param {

    private List<SpecBean> spec;
    private List<SkuBean> sku;

    public List<SpecBean> getSpec() {
        return spec;
    }

    public void setSpec(List<SpecBean> spec) {
        this.spec = spec;
    }

    public List<SkuBean> getSku() {
        return sku;
    }

    public void setSku(List<SkuBean> sku) {
        this.sku = sku;
    }

    public static class SpecBean {
        /**
         * specName : 颜色
         * specValue : ["黑色","红色","粉色","白色","蓝色"]
         */

        private String specName;
        private List<String> specValue;

        public String getSpecName() {
            return specName;
        }

        public void setSpecName(String specName) {
            this.specName = specName;
        }

        public List<String> getSpecValue() {
            return specValue;
        }

        public void setSpecValue(List<String> specValue) {
            this.specValue = specValue;
        }
    }

    public static class SkuBean {
        /**
         * inventoryCount : 0
         * id : 355
         * spec : ["黑色","80g"]
         */

        private int inventoryCount;
        private int id;
        private List<String> spec;

        public SkuBean(int inventoryCount, int id, List<String> spec) {
            this.inventoryCount = inventoryCount;
            this.id = id;
            this.spec = spec;
        }

        public int getInventoryCount() {
            return inventoryCount;
        }

        public void setInventoryCount(int inventoryCount) {
            this.inventoryCount = inventoryCount;
        }

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public List<String> getSpec() {
            return spec;
        }

        public void setSpec(List<String> spec) {
            this.spec = spec;
        }
    }
}
