package com.czxy.bos.elasticsearch.dao;

import com.czxy.bos.elasticsearch.domain.Book;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

import java.util.List;

/** 编写dao层，必须继承通用接口 ElasticsearchRepository ,也就是说父接口中已经完成增删改查操作
 *
 */
public interface BookRepository extends ElasticsearchRepository<Book ,Integer>{

    /**
     * 标题进行等值查询
     * @param title
     * @return
     */
    public Book findByTitle(String title);

    /**
     * 模糊查询
     * @param title
     * @return
     */
    public List<Book> findByTitleLike(String title);

    /**
     * 模糊查询 + 分页
     * @param title
     * @return
     */
    public Page<Book> findByTitleLike(String title , Pageable pageable);


}
