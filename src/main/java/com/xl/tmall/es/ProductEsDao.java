package com.xl.tmall.es;

import com.xl.tmall.pojo.Product;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

//要和加了redis的jpadao放在不同包下
public interface ProductEsDao extends ElasticsearchRepository<Product,Integer> {
}
