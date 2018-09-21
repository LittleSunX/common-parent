package com.czxy.bos.controller.base;

import com.czxy.bos.domain.base.SubArea;
import com.czxy.bos.service.base.SubAreaService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;

@RestController
@RequestMapping("/subArea")
public class SubAreaController {
    @Resource
    private SubAreaService subAreaService;
    @GetMapping("/findPartition")
    public ResponseEntity<List<SubArea>> findPartition(String subAreaId){
        List<SubArea> list = subAreaService.findPartition(subAreaId);
        return  new ResponseEntity<>(list,HttpStatus.OK);
    }
}
