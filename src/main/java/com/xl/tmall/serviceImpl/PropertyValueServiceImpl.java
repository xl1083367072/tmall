package com.xl.tmall.serviceImpl;

import com.xl.tmall.dao.PropertyValueDao;
import com.xl.tmall.pojo.Product;
import com.xl.tmall.pojo.Property;
import com.xl.tmall.pojo.PropertyValue;
import com.xl.tmall.service.PropertyService;
import com.xl.tmall.service.PropertyValueService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PropertyValueServiceImpl implements PropertyValueService {

    @Autowired
    PropertyValueDao propertyValueDao;
    @Autowired
    PropertyService propertyService;

    @Override
    public void update(PropertyValue propertyValue) {
        propertyValueDao.save(propertyValue);
    }

    @Override
    public void init(Product product) {
//        先查询指定商品的分类
        List<Property> properties = propertyService.findByCategory(product.getCategory());
//        遍历该分类的所有属性
        for (Property property:properties){
//            查询该商品指定属性的属性值
            PropertyValue propertyValue = propertyValueDao.findByProductAndProperty(product, property);
//            若没有此属性值,则初始化此属性值,没有添加属性值的方法,通过给分类添加属性,若需要设置商品的属性值,则修改初始化后的属性值
            if(propertyValue==null){
                propertyValue = new PropertyValue();
                propertyValue.setProduct(product);
                propertyValue.setProperty(property);
                propertyValueDao.save(propertyValue);
            }
        }
    }

    @Override
    public List<PropertyValue> findByProductOrderByIdDesc(Product product) {
        return propertyValueDao.findByProductOrderByIdDesc(product);
    }

    @Override
    public PropertyValue findByProductAndProperty(Product product, Property property) {
        return propertyValueDao.findByProductAndProperty(product,property);
    }

    @Override
    public void deleteByProductOrProperty(Product product, Property property) {
        propertyValueDao.deleteByProductOrProperty(product,property);
    }
}
