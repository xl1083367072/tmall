package com.xl.tmall.serviceImpl;

import com.xl.tmall.dao.ProductImageDao;
import com.xl.tmall.pojo.Product;
import com.xl.tmall.pojo.ProductImage;
import com.xl.tmall.service.ProductImageService;
import com.xl.tmall.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProductImageServiceImpl implements ProductImageService {

    @Autowired
    ProductImageDao productImageDao;
    @Autowired
    ProductService productService;

    @Override
    public List<ProductImage> listSingleTypeImage(Product product) {
        List<ProductImage> productImages = productImageDao.findByProductAndTypeOrderByIdDesc(product, SINGLETYPE);
        return productImages;
    }

    @Override
    public List<ProductImage> listDetailTypeImage(Product product) {
        List<ProductImage> productImages = productImageDao.findByProductAndTypeOrderByIdDesc(product, DETAILTYPE);
        return productImages;
    }

    @Override
    public ProductImage findById(int id) {
        ProductImage productImage = productImageDao.getOne(id);
        return productImage;
    }

    @Override
    public void save(ProductImage productImage) {
        productImageDao.save(productImage);
    }

    @Override
    public void delete(int id) {
        productImageDao.deleteById(id);
    }

    @Override
    public void setFirstImage2Product(Product product) {
        List<ProductImage> productImages = listSingleTypeImage(product);
        if(!productImages.isEmpty())
            product.setFirstProductImage(productImages.get(0));
        else
            product.setFirstProductImage(new ProductImage());
    }

    @Override
    public void setFirstImage2Products(List<Product> products) {
        for (Product product:products){
            setFirstImage2Product(product);
        }
    }
}
