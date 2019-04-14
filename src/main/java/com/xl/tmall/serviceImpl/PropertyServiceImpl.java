package com.xl.tmall.serviceImpl;

import com.xl.tmall.dao.PropertyDao;
import com.xl.tmall.pojo.Category;
import com.xl.tmall.pojo.Property;
import com.xl.tmall.service.CategoryService;
import com.xl.tmall.service.PropertyService;
import com.xl.tmall.service.PropertyValueService;
import com.xl.tmall.utils.PageNavigator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class PropertyServiceImpl implements PropertyService {

    @Autowired
    PropertyDao propertyDao;
    @Autowired
    CategoryService categoryService;
    @Autowired
    PropertyValueService propertyValueService;

    @Override
    public Property findById(int id) {
        return propertyDao.getOne(id);
    }

    @Override
    public void save(Property property) {
        propertyDao.save(property);
    }

    @Transactional
    @Override
    public void delete(int id) {
        Property property = new Property();
        property.setId(id);
        propertyValueService.deleteByProductOrProperty(null,property);
        propertyDao.deleteById(id);
    }

    @Override
    public void update(Property property) {
        propertyDao.save(property);
    }

    @Override
    public PageNavigator<Property> findAll(int cid, int start, int size, int navigatePages) {
        Category category = categoryService.findById(cid);
        Sort sort = new Sort(Sort.Direction.DESC,"id");
        Pageable pageable = new PageRequest(start,size,sort);
        Page<Property> page = propertyDao.findByCategory(category, pageable);
        return new PageNavigator<>(page,navigatePages);
    }

    @Override
    public List<Property> findByCategory(Category category) {
        return propertyDao.findByCategory(category);
    }
}
