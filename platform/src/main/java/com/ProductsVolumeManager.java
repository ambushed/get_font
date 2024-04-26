package com;

public class ProductsVolumeManager {

    // For display in UI
    private ProductsVolumeDisplayData displayData = new ProductsVolumeDisplayData();

    public ObservableCopyOnWriteArrayList<ProductVolumeUIModel> getProductVolumesUIList(boolean stocks) {
        return this.displayData.productVolumesUIList;
    }

}
