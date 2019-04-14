package com.xl.tmall.dao;

import com.xl.tmall.pojo.Category;
import org.springframework.data.jpa.repository.JpaRepository;

//springdata中的接口,不需要自己再写curd了
public interface CategoryDao extends JpaRepository<Category,Integer> {
}
