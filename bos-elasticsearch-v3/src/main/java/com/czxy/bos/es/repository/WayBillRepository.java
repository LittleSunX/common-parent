package com.czxy.bos.es.repository;

import com.czxy.bos.es.domain.ESWayBill;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

public interface WayBillRepository  extends ElasticsearchRepository<ESWayBill,String> {

}
