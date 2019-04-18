package com.xl.tmall.serviceImpl;

import com.xl.tmall.dao.ProductDao;
import com.xl.tmall.es.ProductEsDao;
import com.xl.tmall.pojo.Category;
import com.xl.tmall.pojo.Product;
import com.xl.tmall.service.*;
import com.xl.tmall.utils.PageNavigator;
import org.elasticsearch.common.lucene.search.function.FunctionScoreQuery;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.index.query.functionscore.FunctionScoreQueryBuilder;
import org.elasticsearch.index.query.functionscore.ScoreFunctionBuilders;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.elasticsearch.core.query.NativeSearchQueryBuilder;
import org.springframework.data.elasticsearch.core.query.SearchQuery;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
public class ProductServiceImpl implements ProductService {

    @Autowired
    ProductDao productDao;
    @Autowired
    CategoryService categoryService;
    @Autowired
    PropertyValueService propertyValueService;
    @Autowired
    ProductImageService productImageService;
    @Autowired
    OrderItemService orderItemService;
    @Autowired
    ReviewService reviewService;
    @Autowired
    ProductEsDao productEsDao;

    @Override
    public PageNavigator<Product> findAll(int cid, int start, int size, int navigatePages) {
        Category category = categoryService.findById(cid);
        Sort sort = new Sort(Sort.Direction.DESC,"id");
        Pageable pageable = new PageRequest(start,size,sort);
        Page<Product> page = productDao.findByCategory(category, pageable);
        return new PageNavigator<>(page,navigatePages);
    }

    @Override
    public Product findById(int id) {
        return productDao.getOne(id);
    }

    @Override
    public void save(Product product) {
        productDao.save(product);
//        同时更新es服务器中数据
        productEsDao.save(product);
    }

    @Transactional
    @Override
    public void delete(int id) {
        Product product = new Product();
        product.setId(id);
        propertyValueService.deleteByProductOrProperty(product,null);
        productDao.deleteById(id);
        productEsDao.deleteById(id);
    }

    @Override
    public void update(Product product) {
        productDao.save(product);
        productEsDao.save(product);
    }

//    为分类填充商品内容和商品图片
    @Override
    public void fill(Category category) {
        List<Product> products = findByCategory(category);
        productImageService.setFirstImage2Products(products);
        category.setProducts(products);
    }

    @Override
    public void fill(List<Category> categories) {
        for (Category category:categories){
            fill(category);
        }
    }

//    让每个分类每行显示至多8个商品
    @Override
    public void fillByRow(List<Category> categories) {
        int productsByRow = 8;
        for (Category category:categories){
//            拿到一个分类的所有商品
            List<Product> products = category.getProducts();
//            多行显示,所以会有集合的集合
            List<List<Product>> listProducts = new ArrayList<>();
//            每行显示8个,步长为8,即每次循环将8个商品一行作为集合加入行集合中
            for (int i=0;i<products.size();i+=productsByRow){
                int size = i+productsByRow;
//                若此行不足8个,则有多少显示多少个
                size = size>products.size()?products.size():size;
//                将商品集合截取一段放入行集合中
                List<Product> list = products.subList(i, size);
                listProducts.add(list);
            }
//            设置分类的行集合
            category.setProductsByRow(listProducts);
        }
    }

    @Override
    public List<Product> findByCategory(Category category) {
        return productDao.findByCategoryOrderById(category);
    }

//    设置商品销量和评价数量
    @Override
    public void setSaleAndReviewCount(Product product) {
        int saleCount = orderItemService.getSaleCount(product);
        product.setSaleCount(saleCount);
        int reviewCount = reviewService.getReviewCount(product);
        product.setReviewCount(reviewCount);
    }

    @Override
    public void setSaleAndReviewCount(List<Product> products) {
        for (Product product:products){
            setSaleAndReviewCount(product);
        }
    }

    /*@Override
    public List<Product> findByKeyword(String keyword, int start, int size) {
        Sort sort = new Sort(Sort.Direction.DESC,"id");
        Pageable pageable = new PageRequest(start,size,sort);
        List<Product> products = productDao.findByNameLike("%" + keyword + "%", pageable);
        return products;
    }*/

    @Override
    public List<Product> findByKeyword(String keyword, int start, int size) {
        initDatabase2Es();
        FunctionScoreQueryBuilder queryBuilder = QueryBuilders.functionScoreQuery(
                QueryBuilders.matchPhrasePrefixQuery("name",keyword),
                ScoreFunctionBuilders.weightFactorFunction(100))
                .scoreMode(FunctionScoreQuery.ScoreMode.SUM)
                .setMinScore(10);              //关键字查询匹配器
        Sort sort = new Sort(Sort.Direction.DESC,"id");
        Pageable pageable = new PageRequest(start,size,sort);
        SearchQuery searchQuery = new NativeSearchQueryBuilder().withPageable(pageable)
                .withQuery(queryBuilder).build();
        Page<Product> page = productEsDao.search(searchQuery);
        return page.getContent();
    }

    //    初始化数据库数据到es服务器中
    private void initDatabase2Es(){
        Pageable pageable = new PageRequest(0,5);
//        从es服务器中查数据
        Page<Product> page = productEsDao.findAll(pageable);
//        若es中没有该数据,则从数据库中查找所有数据添加到es服务器中
        if(page.getContent().isEmpty()){
            List<Product> products = productDao.findAll();
            for (Product product:products){
                productEsDao.save(product);
            }
        }
    }
}
