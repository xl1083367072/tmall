package com.xl.tmall.controller;

import com.xl.tmall.pojo.Category;
import com.xl.tmall.service.CategoryService;
import com.xl.tmall.utils.ImageUtil;
import com.xl.tmall.utils.PageNavigator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletRequest;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

@RestController
public class CategoryController {

    @Autowired
    CategoryService categoryService;

    @GetMapping("/categories")
    public PageNavigator<Category> finaAll(@RequestParam(value = "start",defaultValue = "0")int start,
                                  @RequestParam(value = "size",defaultValue = "10")int size){
        start = start<0?1:start;
        PageNavigator<Category> pageNavigator = categoryService.findAll(start, size, 5);
        return pageNavigator;
    }

    @PostMapping("/categories")
    public Category save(Category category,MultipartFile file,HttpServletRequest request){
        categoryService.save(category);
        saveOrUpdateImage(category,file,request);
        return category;
    }

    public void saveOrUpdateImage(Category category, MultipartFile file, HttpServletRequest request){
        File filepath = new File(request.getServletContext().getRealPath("tmall/img/category"));
        File path = new File(filepath,category.getId()+".jpg");
        if(!path.getParentFile().exists()){
            path.getParentFile().mkdirs();
        }
        try {
            file.transferTo(path);
            BufferedImage bufferedImage = ImageUtil.change2Jpg(path);
            ImageIO.write(bufferedImage,"jpg",path);
        }catch (IOException e){
            e.printStackTrace();
            System.out.println("上传图片失败");
        }
    }

    @DeleteMapping("/categories/{id}")
    public String delete(@PathVariable("id")int id,HttpServletRequest request){
        categoryService.delete(id);
//        删除分类图片
        File filepath = new File(request.getServletContext().getRealPath("tmall/img/category"));
        File file = new File(filepath,id+".jpg");
        file.delete();
        return null;
    }

//    之所以把id放在url里面,而不直接由pojo接收,是因为要和查询所有的映射区分开来
    @GetMapping("/categories/{id}")
    public Category findById(@PathVariable("id") int id){
        Category category = categoryService.findById(id);
        return category;
    }

    @PutMapping("/categories")
    public Category update(Category category,MultipartFile file,HttpServletRequest request){
        categoryService.update(category);
        if(file!=null){
            saveOrUpdateImage(category,file,request);
        }
//        要返回一个对象,不然更新后的图片不刷新
        return category;
    }

}
