package com.czxy.bos.elasticsearch.service;

import com.czxy.bos.elasticsearch.dao.BookRepository;
import com.czxy.bos.elasticsearch.domain.Book;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.List;

/**
 * Created by liangtong on 2018/9/27.
 */
@Service
@Transactional
public class BookService {

    @Resource
    private BookRepository bookRepository;

    /**
     * 添加数据 （如果id重复，为更新数据）
     * @param book
     */
    public void save(Book book ){
        this.bookRepository.save(book);
    }

    public Page<Book> findAll(int page , int rows){
        Pageable pageable = PageRequest.of( page-1,rows);
        return this.bookRepository.findAll(pageable);
    }

    public Book findByTtile(String title){
        return this.bookRepository.findByTitle(title);
    }

    public List<Book> findByTitleLike(String title){
        return this.bookRepository.findByTitleLike(title);
    }

    public Page<Book> findByTitleLike(String title,int page, int size){
        return this.bookRepository.findByTitleLike(title , PageRequest.of(page-1,size));
    }

}
