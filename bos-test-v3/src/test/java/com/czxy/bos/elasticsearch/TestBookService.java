package com.czxy.bos.elasticsearch;

import com.czxy.bos.TestApplication;
import com.czxy.bos.elasticsearch.domain.Book;
import com.czxy.bos.elasticsearch.service.BookService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import javax.annotation.Resource;
import java.util.List;

/**
 * Created by liangtong on 2018/9/27.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes= TestApplication.class)     //指定启动类
public class TestBookService {

    @Resource
    private BookService bookService;

    @Test
    public void testSave(){
        Book book = new Book();
        book.setId(3);
        book.setTitle("Java骨灰级");
        book.setContent("准备下课");

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


}
