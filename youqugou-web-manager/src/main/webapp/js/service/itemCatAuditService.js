//服务层
/**
 * 贾运通 2018-12-31
 */
app.service("itemCatAuditService", function ($http) {

    this.searchItemCatAudit = function (currentPage, itemsPerPage, searchEntity) {
        return $http.post("../itemCatAudit/searchItemCatAudit.do?pageNum=" + currentPage
            + "&pageSize=" + itemsPerPage, searchEntity);
    }



    /*修改状态*/
    this.updateStatus = function (ids,status) {
        return $http.get('../itemCatAudit/updateStatus.do?ids='+ids+"&status="+status);
    }


    //读取列表数据绑定到表单中
    this.selectAllItemCats=function(){
        return $http.get('../itemCatAudit/selectAllItemCats.do');
    }
    //分页
    this.findPage = function (page, rows) {
        return $http.get('../itemCatAudit/findPage.do?page=' + page + '&rows=' + rows);
    }
    //查询实体
    this.selectItemCatById = function (id) {
        return $http.get('../itemCatAudit/selectItemCatById.do?id=' + id);
    }
    //增加
    this.insertItemCat = function (entity) {
        return $http.post('../itemCatAudit/insertItemCat.do', entity);
    }
    //修改
    this.updateItemCat = function (entity) {
        return $http.post('../itemCatAudit/updateItemCat.do', entity);
    }
    //删除
    this.dele = function (ids) {
        return $http.get('../itemCatAudit/delete.do?ids=' + ids);
    }
    //搜索
    this.search = function (page, rows, searchEntity) {
        return $http.post('../itemCatAudit/search.do?page=' + page + "&rows=" + rows, searchEntity);
    }

    this.selectItemCatsMapByParentId = function (parentId) {
        return $http.get("../itemCatAudit/selectItemCatsMapByParentId.do?parentId=" + parentId);
    }
});
