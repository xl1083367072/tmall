package com.xl.tmall.controller;

import com.xl.tmall.pojo.Product;
import com.xl.tmall.pojo.ProductImage;
import com.xl.tmall.service.ProductImageService;
import com.xl.tmall.service.ProductService;
import com.xl.tmall.utils.ImageUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletRequest;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@RestController
public class ProductImageController {

    @Autowired
    ProductImageService productImageService;
    @Autowired
    ProductService productService;

    @GetMapping("/products/{pid}/productImages")
    public List<ProductImage> findAll(@PathVariable("pid")int pid, @RequestParam("type")String type){
        Product product = productService.findById(pid);
        if(productImageService.SINGLETYPE.equals(type)){
            List<ProductImage> productImages = productImageService.listSingleTypeImage(product);
            return productImages;
        }else if (productImageService.DETAILTYPE.equals(type)){
            List<ProductImage> productImages = productImageService.listDetailTypeImage(product);
            return productImages;
        }else {
            return new ArrayList<>();
        }
    }

    @PostMapping("productImages")
    public ProductImage save(String type,int pid, MultipartFile file, HttpServletRequest request){
        Product product = productService.findById(pid);
        ProductImage productImage = new ProductImage();
        productImage.setType(type);
        productImage.setProduct(product);
//        先保存商品图片信息
        productImageService.save(productImage);
//        构建商品图片保存路径
        String filename = "img/";
        if(productImageService.SINGLETYPE.equals(productImage.getType()))
            filename += "productSingle";
        else
            filename += "productDetail";
        File filepath = new File(request.getServletContext().getRealPath(filename));
        File path = new File(filepath,productImage.getId()+".jpg");
        String name = path.getName();
        if(!path.getParentFile().exists())
            path.getParentFile().mkdirs();
        try {
//            复制图片
            file.transferTo(path);
            BufferedImage image = ImageUtil.change2Jpg(path);
            ImageIO.write(image,"jpg",path);
        }catch (IOException e){
            e.printStackTrace();
        }
        if(productImageService.SINGLETYPE.equals(productImage.getType())){
            String middleImage = request.getServletContext().getRealPath("img/productSingle_middle");
            String smallImage = request.getServletContext().getRealPath("img/productSingle_small");
            File middleFile = new File(middleImage,filename);
            File smallFile = new File(smallImage,filename);
            if(!middleFile.getParentFile().exists())
                middleFile.getParentFile().mkdirs();
            if(!smallFile.getParentFile().exists())
                smallFile.getParentFile().mkdirs();
            ImageUtil.resizeImage(path,56,56,middleFile);
            ImageUtil.resizeImage(path,217,190,smallFile);
        }
        return productImage;
    }

    @DeleteMapping("/productImages/{id}")
    public String delete(@PathVariable("id") int id,HttpServletRequest request){
        ProductImage productImage = productImageService.findById(id);
//        删除掉图片信息
        productImageService.delete(id);
//        删除图片
        String filename = "img/";
        if(productImageService.SINGLETYPE.equals(productImage.getType()))
            filename += "productSingle";
        else
            filename += "productDetail";
        File filepath = new File(request.getServletContext().getRealPath(filename));
        File path = new File(filepath,productImage.getId()+".jpg");
        String name = path.getName();
//        删除掉原图
        path.delete();
        if(productImageService.equals(productImage.getType())){
//            删除掉缩略图
            String middleImage = request.getServletContext().getRealPath("img/productSingle_middle");
            String smallImage = request.getServletContext().getRealPath("img/productSingle_small");
            File middleFile = new File(middleImage,filename);
            File smallFile = new File(smallImage,filename);
            if(middleFile.exists())
                middleFile.delete();
            if(smallFile.exists())
                smallFile.delete();
        }
        return null;
    }

}
