package com.czxy.bos.elasticsearch.service;

import com.czxy.bos.elasticsearch.dao.BookRepository;
import com.czxy.bos.elasticsearch.domain.Book;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.elasticsearch.core.query.NativeSearchQuery;
import org.springframework.data.elasticsearch.core.query.SearchQuery;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.List;


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
    public List<Book> searchOrder(String title){
        return this.bookRepository.findByTitleLike(title);
    }

    public Page<Book> searchOrder2(String title,Pageable pageable){
        return this.bookRepository.findByTitleLikeOrderByIdAsc(title,pageable);
    }
    public Iterable<Book> findOrderById(){
        //排序基本语法：Sort.by(...) 进行排序
        //   指定字段升序：Sort.Order.asc("字段名")
        //   指定字段降序：Sort.Order.desc("字段名")
        return this.bookRepository.findAll(Sort.by(Sort.Order.asc("id")));
    }
    public Page<Book> searchQuery(int page , int size){

        BoolQueryBuilder queryBuilder = QueryBuilders.boolQuery();

        //等值查询，大小写不敏感，可以检索中文
       // queryBuilder.must(QueryBuilders.matchPhraseQuery("content","java语法学习"));

        //模糊查询，大小写不敏感
        //queryBuilder.must(QueryBuilders.matchQuery("content" , "java"));

        //多字段模糊查询，or或关系
        //queryBuilder.must(QueryBuilders.multiMatchQuery("java","content" , "title"));

        //通过id查询
        //queryBuilder.must(QueryBuilders.idsQuery().addIds("1","5"));

        //模糊查询
       // queryBuilder.must(QueryBuilders.fuzzyQuery("content", "java"));

        //范围内查询
        queryBuilder.must(QueryBuilders.rangeQuery("id").from(2).to("5"));

      SearchQuery searchQuery = new NativeSearchQuery( queryBuilder );
        //分页查询
        //searchQuery.setPageable( PageRequest.of(page - 1 ,size) );
        //排序查询（字符串默认不允许排序）
        //searchQuery.addSort(Sort.by(Sort.Order.asc("id")));
        //searchQuery.addSort(Sort.by(Sort.Order.asc("title"), Sort.Order.asc("content")));

        return this.bookRepository.search( searchQuery );
    }




}
