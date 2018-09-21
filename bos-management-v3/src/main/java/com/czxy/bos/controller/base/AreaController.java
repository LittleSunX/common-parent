package com.czxy.bos.controller.base;

import com.czxy.bos.domain.base.Area;
import com.czxy.bos.service.base.AreaService;
import com.czxy.bos.vo.EasyUIResult;
import com.github.pagehelper.PageInfo;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import javax.annotation.Resource;
import java.io.File;
import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 *
 */
@RestController
@RequestMapping("/area")
public class AreaController {
    @Resource
    private AreaService areaService;

    /**
     * 文件上传
     * @param file
     * @return
     * @throws Exception
     */
    @PostMapping("/batchImport")
    public ResponseEntity<String> uploadAndBatchImport(MultipartFile  file) throws Exception {
        // System.out.println(file);
        //1 处理excel文件 （注意：必须上传提供模板）
        //1.1 excel文件保存到服务器
        File newFile = new File("F:\\upload" , UUID.randomUUID() + file.getOriginalFilename());
        file.transferTo(newFile);
        //1.2解析excel文件
        //加载xls文件获得工作簿
        Workbook workbook = new HSSFWorkbook(new FileInputStream(newFile));
        //获得第一张表
        Sheet sheetAt = workbook.getSheetAt(0);
        //获得所有行
        int lastRowNum = sheetAt.getLastRowNum();
        // 存放所有解析后的area对象的
        List<Area> areaList=new ArrayList<>();
        for (int i = 0; i <=lastRowNum; i++) {
            // 4) 获得对应单元格
            Row row = sheetAt.getRow(i);
            if(row == null){
                continue;
            }

            String id = row.getCell(0).getStringCellValue();
            String province = row.getCell(1).getStringCellValue();
            String city = row.getCell(2).getStringCellValue();
           // String citycode = row.getCell(2).getStringCellValue();
            String district = row.getCell(3).getStringCellValue();
            String postcode = row.getCell(4).getStringCellValue();

           // String shortcode = row.getCell(6).getStringCellValue();

            //将数据封装到area对象
            Area area = new Area();
            area.setId(id);
            area.setProvince(province);
            area.setCity(city);
            area.setDistrict(district);
            area.setPostcode(postcode);

            // citycode , shortchode 程序自动生成的

            //添加
            areaList.add(area);

        }
        //2 保存所有数据
        int count = areaService.saveAreas(areaList);


        //删除和关闭
        workbook.close();
        newFile.delete();



        return new ResponseEntity<>("本次上传成功"+count+"条",HttpStatus.OK);

    }

    /**
     *查询区域列表页面
     *  @param area
     * @param page
     * @param rows
     * @return
     */
    @GetMapping
    public ResponseEntity<EasyUIResult<Area>> queryAreaListByPage(Area area , @RequestParam("page") Integer page, @RequestParam("rows")Integer rows){
        PageInfo<Area> pageInfo = areaService.queryAreaListByPage(area, page, rows);
        EasyUIResult<Area> result = new EasyUIResult<>(pageInfo.getTotal(), pageInfo.getList());
        return  new ResponseEntity<>(result,HttpStatus.OK);
    }

    /**
     * 添加
     * @param area
     * @return
     */
    @PostMapping
    public ResponseEntity<String> save(Area area){
        Integer count = areaService.save(area);
        if (count==1){
            return  new ResponseEntity<>("添加成功",HttpStatus.CREATED);//201状态码
        }
        return  new ResponseEntity<>("添加失败",HttpStatus.INTERNAL_SERVER_ERROR);//500状态码

    }
    /**
     * 修改
     */
    @PutMapping
    public ResponseEntity<String> update(Area area){
        Integer u = areaService.updata(area);
        if (u==1){
            return  new ResponseEntity<>("修改成功",HttpStatus.OK);

        }
        return  new ResponseEntity<>("修改失败",HttpStatus.INTERNAL_SERVER_ERROR);

    }
    @DeleteMapping
    public  ResponseEntity<String> delete(String ids){

        try {
            String[] idArray = ids.split(",");
            areaService.delete(idArray);
            return  new ResponseEntity<>("删除成功",HttpStatus.OK);
        } catch (Exception e) {
            e.printStackTrace();
            return  new ResponseEntity<>("删除失败",HttpStatus.INTERNAL_SERVER_ERROR);
        }


    }

}
