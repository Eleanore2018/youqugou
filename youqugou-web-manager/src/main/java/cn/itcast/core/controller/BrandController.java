package cn.itcast.core.controller;

import cn.itcast.core.entity.PageResult;
import cn.itcast.core.entity.Result;
import cn.itcast.core.pojo.good.Brand;
import cn.itcast.core.service.BrandService;
import com.alibaba.dubbo.config.annotation.Reference;
import org.apache.commons.lang3.StringUtils;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.text.ParseException;
import java.util.*;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/brand")
public class BrandController {

    @Reference
    private BrandService brandService;

    /**
     * 查询所有brand
     * @return
     */
    @RequestMapping("/selectAllBrandMap")
    public List<Map> selectAllBrandMap() {
        return brandService.selectAllBrandMap();
    }

    /**
     * 查询分页数据
     * @param pageNum
     * @param pageSize
     * @return
     */
    @RequestMapping("/selectPageBrand")
    public PageResult selectPageBrand(Integer pageNum, Integer pageSize) {
        return brandService.selectPageBrand(pageNum, pageSize);
    }

    /**
     * 插入数据
     */
    @RequestMapping("/insertBrand")
    public Result insertBrand(@RequestBody Brand brand) {
        int row = brandService.insertBrand(brand);
        if (row > 0) {
            return new Result(true, "保存成功");
        }
        return new Result(false, "保存失败");
    }

    /**
     * 修改brand
     * @param brand
     * @return
     */
    @RequestMapping("/updateBrand")
    public Result updateBrand(@RequestBody Brand brand) {
        int row = brandService.updateBrand(brand);
        if (row > 0) {
            return new Result(true,"修改成功");
        }
        return new Result(false, "修改失败");
    }

    /**
     * 根据主键查找brand
     * @param id
     * @return
     */
    @RequestMapping("/selectBrandById")
    public Brand selectBrandById(Long id) {
        return brandService.selectBrandById(id);
    }

    /**
     * 根据ids批量删除brand
     * @param ids
     * @return
     */
    @RequestMapping("/deleteBrandByIds")
    public Result deleteBrandByIds(@RequestBody Long[] ids) {
        int row = brandService.deleteBrandByIds(ids);
        if (row > 0) {
            return new Result(true,"删除成功");
        }
        return new Result(false, "删除失败");
    }

    @RequestMapping("/importExcle")
    public Result batchImport(HttpServletRequest request) {
        String filePath = request.getParameter("filePath");
        if (StringUtils.isEmpty(filePath)){
            return new Result(false,"文件路径不存在");
        }
        if (!this.isExcel(filePath)) {
            System.out.println("不是Excel文件");
            return new Result(false,"不是Excel文件");
        }
        boolean isExcel2003 = this.isExcel2003(filePath);
        File file = new File(filePath);
        Workbook wb = null;
        try {
            if (isExcel2003) {
                wb = new HSSFWorkbook(new FileInputStream(file));
            } else {//Excel2007及以后的处理
                wb = new XSSFWorkbook(new FileInputStream(file));
            }
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("create parsing order excel object error. 新建Excel解析类错误");
            return new Result(false,"新建Excel解析类错误");
        }

        //获取第一张表
        Sheet sheet = wb.getSheetAt(0);
        if (sheet == null) {
            System.out.println("get first sheet error. Excel中的第一个sheet为空");
            try {
                wb.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return new Result(false," Excel中的第一个sheet为空");
        }

        int rowNum = sheet.getLastRowNum();
        if (rowNum < 2) {
            try {
                wb.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return new Result(false,"");
        }
        Map<String,Integer> tradeAndGoodIdCount = new HashMap<String,Integer>();
        for (int r=1; r <= rowNum; r++) {
            Row row = sheet.getRow(r);
            if (row == null) continue;

            Brand brand = new Brand();
            double id = row.getCell(0).getNumericCellValue();
            String name = row.getCell(1).getStringCellValue();
            String firstChar = row.getCell(2).getStringCellValue();
            Date birthday = row.getCell(3).getDateCellValue();
            Double idD = new Double(id);
            brand.setId(idD.longValue());
            brand.setName(name);
            brand.setFirstChar(firstChar);
            brandService.insertOrUpdateBrand(brand);
        }

        try {
            wb.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return new Result(true,"解析文件成功!");
    }

    /**
     * 验证是否是Excel 2003的文件
     *
     * @param filePath 待验证文件路径或者文件名
     * @return 如果是符合格式的字符串,返回 <b>true </b>,否则为 <b>false </b>
     */
    public static boolean isExcel2003(String filePath) {
        return filePath.matches("^.+\\.(?i)(xls)$");
    }
    /**
     * 验证是否是Excel 2007的文件
     *
     * @param filePath 待验证文件路径或者文件名
     * @return 如果是符合格式的字符串,返回 <b>true </b>,否则为 <b>false </b>
     */
    public static boolean isExcel2007OrLater(String filePath) {
        return filePath.matches("^.+\\.(?i)(xlsx)$");
    }

    /**
     * 验证是否是Excel的文件
     *
     * @param filePath 待验证文件路径或者文件名
     * @return 如果是符合格式的字符串,返回 <b>true </b>,否则为 <b>false </b>
     */
    public static boolean isExcel(String filePath) {
        if (filePath == null || !(isExcel2003(filePath) || isExcel2007OrLater(filePath))) {
            return false;
        }
        return true;
    }
    /**
     * 王浩宇修改于2018/12/30,接口中参数添加了一个String name
     */
    @RequestMapping("/searchBrand")
    public PageResult searchBrand(Integer pageNum, Integer pageSize, @RequestBody Brand brand,String name) {
        return brandService.searchBrand(pageNum, pageSize, brand,name);
    }

    @RequestMapping("/searchBrand")
    public PageResult searchBrand(Integer pageNum, Integer pageSize, @RequestBody Brand brand) {
        return brandService.searchBrand(pageNum, pageSize, brand);
    }


    /*贾运通*/
    //品牌审核
    @RequestMapping("/updateStatus")
    public Result updateStatus(Long[] ids, String status) {
        try {
            brandService.updateStatus(ids,status);
            return new Result(true,"成功");
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false,"失败");
        }
    }
}

