package cn.itcast.core.service;

import cn.itcast.core.dao.ad.ContentDao;
import cn.itcast.core.entity.PageResult;
import cn.itcast.core.pojo.ad.Content;
import cn.itcast.core.pojo.ad.ContentQuery;
import com.alibaba.dubbo.config.annotation.Service;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
@Transactional
public class ContentServiceImpl implements ContentService {

    @Autowired
    private ContentDao contentDao;
    @Autowired
    private RedisTemplate redisTemplate;

    @Override
    public List<Content> findAll() {
        List<Content> list = contentDao.selectByExample(null);
        return list;
    }

    @Override
    public PageResult findPage(Content content, Integer pageNum, Integer pageSize) {
        PageHelper.startPage(pageNum, pageSize);
        Page<Content> page = (Page<Content>) contentDao.selectByExample(null);
        return new PageResult(page.getTotal(), page.getResult());
    }

    @Override
    public void add(Content content) {
        contentDao.insertSelective(content);
        redisTemplate.boundHashOps("content").delete(content.getCategoryId());
    }

    @Override
    public void edit(Content content) {
        // 修改后的分类id,有可能修改了有可能没修改,所以先根据id查询出修改前的分类id
        Content c = contentDao.selectByPrimaryKey(content.getId());
        // 保存用户修改的数据
        contentDao.updateByPrimaryKeySelective(content);
        if (!c.getCategoryId().equals(content.getCategoryId())) {
            // 如果两次不相等,则更新两处的缓存库
            redisTemplate.boundHashOps("content").delete(c.getCategoryId());
        }
        redisTemplate.boundHashOps("content").delete(content.getCategoryId());
    }

    @Override
    public Content findOne(Long id) {
        Content content = contentDao.selectByPrimaryKey(id);
        return content;
    }

    @Override
    public void delAll(Long[] ids) {
        Set<Long> set = new HashSet<>();
        if (ids != null) {
            for (Long id : ids) {
                Content content = contentDao.selectByPrimaryKey(id);
                set.add(content.getCategoryId());
                contentDao.deleteByPrimaryKey(id);
            }
        }
        redisTemplate.boundHashOps("content").delete(set);
    }

    @Override
    public List<Content> selectContentsByCategoryId(Long categoryId) {
        // 先查询缓存中是否有广告数据
        List<Content> contentList = (List<Content>) redisTemplate.boundHashOps("content").get(categoryId);
        if (null == contentList) {
            // 没有 则先去数据库查询
            ContentQuery contentQuery = new ContentQuery();
            // 按照sort_order字段降序
            contentQuery.setOrderByClause("sort_order desc");
            // 根据categoryId查询状态码为1的广告
            contentQuery.createCriteria().andCategoryIdEqualTo(categoryId).andStatusEqualTo("1");
            contentList = contentDao.selectByExample(contentQuery);
            // hashOperations相当于map,存给缓存一份
            redisTemplate.boundHashOps("content").put(categoryId, contentList);
        }
        return contentList;
    }
}
