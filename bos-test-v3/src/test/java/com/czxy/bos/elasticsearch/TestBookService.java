package com.czxy.bos.elasticsearch;

import com.czxy.bos.TestApplication;
import com.czxy.bos.elasticsearch.domain.Book;
import com.czxy.bos.elasticsearch.service.BookService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import javax.annotation.Resource;
import java.util.List;


@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes= TestApplication.class)     //指定启动类
public class TestBookService {

    @Resource
    private BookService bookService;

    @Test
    public void testSave(){
        Book book = new Book();
        book.setId(10);
        book.setTitle("Java000级");
        book.setContent("呵呵呵");

        bookService.save(book);

        System.out.println("保存成功");
    }

    @Test
    public void testFindAll(){
        Page<Book> page = this.bookService.findAll(2,2);

        System.out.println("总分页：" +  page.getTotalPages());
        System.out.println("总个数：" + page.getTotalElements());
        System.out.println("每页显示个数：" +  page.getSize());

        List<Book> list = page.getContent();
        System.out.println(list.size());
    }

    @Test
    public void testFind1(){

        Book book = bookService.findByTtile("Java入门");

        System.out.println(book);
    }

    @Test
    public void testFind2(){

        List<Book> list = bookService.findByTitleLike("Java");

        for (Book book: list ) {
            System.out.println(book);
        }
    }

    @Test
    public void testFind3(){

        Page<Book> list = bookService.findByTitleLike("Java",1,2);

        for (Book book: list.getContent() ) {
            System.out.println(book);
        }
    }
    @Test
    public void findAll4(){
        Pageable pageable = PageRequest.of(0,10);
        //id 排序
        List<Book> all = bookService.searchOrder("java");
        for(Book book : all){
            System.out.println(book);
        }
    }

    @Test
    public void findAll5(){
        Pageable pageable = PageRequest.of(0,10);
        //id 排序
        Page<Book> all = bookService.searchOrder2("java",pageable);
        System.out.println(all.getTotalPages());
        for(Book book : all.getContent()){
            System.out.println(book);
        }
    }

    /**
     * 查询所有，排序
     */
    @Test
    public void testFind5(){

        Iterable<Book> list = bookService.findOrderById();

        for (Book book: list ) {
            System.out.println(book);
        }
    }

    @Test
    public void findAll6(){
        //id 排序
        Page<Book> all = bookService.searchQuery(1,2);
        System.out.println("本次数据条数：" + all.getNumberOfElements());
        for(Book book : all.getContent()){
            System.out.println(book);
        }
    }




}
