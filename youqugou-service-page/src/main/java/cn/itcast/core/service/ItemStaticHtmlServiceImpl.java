package cn.itcast.core.service;

import cn.itcast.core.dao.good.GoodsDao;
import cn.itcast.core.dao.good.GoodsDescDao;
import cn.itcast.core.dao.item.ItemCatDao;
import cn.itcast.core.dao.item.ItemDao;
import cn.itcast.core.pojo.good.Goods;
import cn.itcast.core.pojo.item.ItemQuery;
import com.alibaba.dubbo.config.annotation.Service;
import freemarker.template.Configuration;
import freemarker.template.Template;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.context.ServletContextAware;
import org.springframework.web.servlet.view.freemarker.FreeMarkerConfigurer;

import javax.servlet.ServletContext;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.util.HashMap;
import java.util.Map;

@Service
public class ItemStaticHtmlServiceImpl implements ItemStaticHtmlService, ServletContextAware {
    @Autowired
    private FreeMarkerConfigurer freeMarkerConfigurer;

    private ServletContext servletContext;
    @Autowired
    private ItemDao itemDao;
    @Autowired
    private GoodsDescDao goodsDescDao;
    @Autowired
    private GoodsDao goodsDao;
    @Autowired
    private ItemCatDao itemCatDao;

    public void genItemDetailStaticHtml(Long goodsId) {
        // 生成配置对象
        Configuration configuration = freeMarkerConfigurer.getConfiguration();

        Map<String, Object> map = new HashMap<>();
        // 根据goodsId获取itemList
        ItemQuery itemQuery = new ItemQuery();
        itemQuery.createCriteria().andGoodsIdEqualTo(goodsId);
        map.put("itemList", itemDao.selectByExample(itemQuery));
        // 获取详情页的所有商品图片
        map.put("goodsDesc", goodsDescDao.selectByPrimaryKey(goodsId));
        Goods goods = goodsDao.selectByPrimaryKey(goodsId);
        map.put("goods", goods);
        map.put("itemCat1", itemCatDao.selectByPrimaryKey(goods.getCategory1Id()).getName());
        map.put("itemCat2", itemCatDao.selectByPrimaryKey(goods.getCategory2Id()).getName());
        map.put("itemCat3", itemCatDao.selectByPrimaryKey(goods.getCategory3Id()).getName());

        Writer out = null;
        try {
            // 读取模板对象
            Template template = configuration.getTemplate("item.ftl", "utf-8");
            // 配置输出的静态页路径和编码
            out = new OutputStreamWriter(new FileOutputStream(servletContext.getRealPath(goodsId + ".html")), "utf-8");
            template.process(map, out);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (null != out) {
                    out.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void setServletContext(ServletContext servletContext) {
        this.servletContext = servletContext;
    }
}
