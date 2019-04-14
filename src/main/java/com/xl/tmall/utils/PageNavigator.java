package com.xl.tmall.utils;

import org.springframework.data.domain.Page;

import java.util.List;

//分页导航,封装jpa的分页类Page,格式化显示分页导航,比如4 5 6 7 8 9 10,7前面三页后面三页
public class PageNavigator<T> {

    Page<T> page;               //jpa page对象
    int navigatePages;          //分页导航要显示多少页
    int totalPages;             //总页数
    int number;                 //显示第几页,从0开始
    long totalElements;         //总记录数
    int size;                   //每页显示数量
    int numberOfElements;       //number所在页记录数量
    List<T> content;            //内容
    boolean isHasContent;       //是否有内容
    boolean first;              //是否首页
    boolean last;               //是否尾页
    boolean isHasPrevious;      //是否有上一页
    boolean isHasNext;          //是否有下一页
    int[] navigatePageNums;     //分页导航显示的页码数组

    public PageNavigator() {
    }

    public PageNavigator(Page<T> page, int navigatePages) {
        this.page = page;
        this.navigatePages = navigatePages;
        this.totalPages = page.getTotalPages();
        this.number = page.getNumber();
        this.totalElements = page.getTotalElements();
        this.size = page.getSize();
        this.numberOfElements = page.getNumberOfElements();
        this.content = page.getContent();
        this.isHasContent = page.hasContent();
        this.first = page.isFirst();
        this.last = page.isLast();
        this.isHasPrevious = page.hasPrevious();
        this.isHasNext = page.hasNext();
        calculateNavigatePages();
    }

//    计算分页导航显示页码
    private void calculateNavigatePages() {
//        存放分页导航页码
        int navigatePageNums[];
        int totalPages = getTotalPages();
        int number = getNumber();
//        若总页数小于导航显示页数,则导航显示的页码数就是总页数的页码数
        if(totalPages<=navigatePages){
            navigatePageNums = new int[totalPages];
            for(int i=0;i<totalPages;i++){
                navigatePageNums[i] = i+1;
            }
        }else {
//            否则,按导航页码数显示页码
            navigatePageNums = new int[navigatePages];
//            取该显示样式起始页
            int start = number - navigatePages/2;
//            尾页
            int end  = number + navigatePages/2;
//            若起始页小于1,则从1开始显示
            if(start<1){
                start = 1;
                for(int i=0;i<navigatePages;i++){
                    navigatePageNums[i] = start++;
                }
            }else if (end>totalPages){
//                若尾页大于总页数,则尾页就是总页
                end = totalPages;
                for(int i=navigatePages-1;i>=0;i--){
                    navigatePageNums[i] = end--;
                }
            }else {
//                否则,正常从起始页组成导航分页页码数组
                for(int i=0;i<navigatePages;i++){
                    navigatePageNums[i] = start++;
                }
            }

        }
        this.navigatePageNums = navigatePageNums;
    }

    public Page<T> getPage() {
        return page;
    }

    public void setPage(Page<T> page) {
        this.page = page;
    }

    public int getNavigatePages() {
        return navigatePages;
    }

    public void setNavigatePages(int navigatePages) {
        this.navigatePages = navigatePages;
    }

    public int getTotalPages() {
        return totalPages;
    }

    public void setTotalPages(int totalPages) {
        this.totalPages = totalPages;
    }

    public int getNumber() {
        return number;
    }

    public void setNumber(int number) {
        this.number = number;
    }

    public long getTotalElements() {
        return totalElements;
    }

    public void setTotalElements(long totalElements) {
        this.totalElements = totalElements;
    }

    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
    }

    public int getNumberOfElements() {
        return numberOfElements;
    }

    public void setNumberOfElements(int numberOfElements) {
        this.numberOfElements = numberOfElements;
    }

    public List<T> getContent() {
        return content;
    }

    public void setContent(List<T> content) {
        this.content = content;
    }

    public boolean isHasContent() {
        return isHasContent;
    }

    public void setHasContent(boolean hasContent) {
        isHasContent = hasContent;
    }

    public boolean isFirst() {
        return first;
    }

    public void setFirst(boolean first) {
        this.first = first;
    }

    public boolean isLast() {
        return last;
    }

    public void setLast(boolean last) {
        this.last = last;
    }

    public boolean isHasPrevious() {
        return isHasPrevious;
    }

    public void setHasPrevious(boolean hasPrevious) {
        isHasPrevious = hasPrevious;
    }

    public boolean isHasNext() {
        return isHasNext;
    }

    public void setHasNext(boolean hasNext) {
        isHasNext = hasNext;
    }

    public int[] getNavigatePageNums() {
        return navigatePageNums;
    }

    public void setNavigatePageNums(int[] navigatePageNums) {
        this.navigatePageNums = navigatePageNums;
    }
}
