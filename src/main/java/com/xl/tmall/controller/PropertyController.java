package com.xl.tmall.controller;

import com.xl.tmall.pojo.Property;
import com.xl.tmall.service.PropertyService;
import com.xl.tmall.utils.PageNavigator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
public class PropertyController {

    @Autowired
    PropertyService propertyService;

    @GetMapping("/categories/{cid}/properties")
    public PageNavigator<Property> findAll(@PathVariable("cid")int cid, @RequestParam(value = "start",defaultValue = "0")int start,
                                           @RequestParam(value = "size",defaultValue = "10")int size){
        start = start<0?0:start;
        PageNavigator<Property> pageNavigator = propertyService.findAll(cid, start, size, 5);
        return pageNavigator;
    }

    @GetMapping("/properties/{id}")
    public Property findById(@PathVariable("id")int id){
        Property property = propertyService.findById(id);
        return property;
    }

    @PostMapping("/properties")
    public Property save(@RequestBody Property property){
        propertyService.save(property);
        return property;
    }

    @DeleteMapping("/properties/{id}")
    public String delete(@PathVariable("id")int id){
        propertyService.delete(id);
        return null;
    }

    @PutMapping("/properties")
    public Property update(@RequestBody Property property){
        propertyService.save(property);
        return property;
    }

}
