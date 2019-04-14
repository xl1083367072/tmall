package com.xl.tmall.serviceImpl;

import com.xl.tmall.dao.CategoryDao;
import com.xl.tmall.pojo.Category;
import com.xl.tmall.pojo.Product;
import com.xl.tmall.service.CategoryService;
import com.xl.tmall.utils.PageNavigator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CategoryServiceImpl implements CategoryService {

    @Autowired
    CategoryDao categoryDao;

//    查询所有
    @Override
    public List<Category> findAll() {
        Sort sort = new Sort(Sort.Direction.DESC,"id");
        List<Category> categories = categoryDao.findAll(sort);
        return categories;
    }

//    分页查询
    @Override
    public PageNavigator<Category> findAll(int start, int size, int navigatePages) {
//        根据id倒序显示,可以将最新插入的数据显示在最前面
        Sort sort = new Sort(Sort.Direction.DESC,"id");
        Pageable pageable = new PageRequest(start, size, sort);
        Page<Category> page = categoryDao.findAll(pageable);
        return new PageNavigator<>(page,navigatePages);
    }

    @Override
    public void save(Category category) {
        categoryDao.save(category);
    }

    @Override
    public void delete(int id) {
        categoryDao.deleteById(id);
    }

    @Override
    public Category findById(int id) {
        return categoryDao.getOne(id);
    }

    @Override
    public void update(Category category) {
        categoryDao.save(category);
    }

    //    将product单方面解除与category的联系,否则会无限循环,而且业务也不需要根据product查category
    @Override
    public void removeCategoryFromProduct(Category category) {
        List<Product> products = category.getProducts();
        if(products!=null){
            for (Product product:products){
                product.setCategory(null);
            }
        }
        List<List<Product>> listProducts = category.getProductsByRow();
        if(listProducts!=null){
            for (List<Product> list:listProducts){
                for (Product product:list){
                    product.setCategory(null);
                }
            }
        }
    }

    @Override
    public void removeCategoryFromProduct(List<Category> categories) {
        for (Category category:categories){
            removeCategoryFromProduct(category);
        }
    }
}
