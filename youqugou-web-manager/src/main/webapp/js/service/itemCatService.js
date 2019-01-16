//服务层
app.service("itemCatService", function ($http) {

    this.searchItemCat = function (currentPage, itemsPerPage, searchEntity) {
        return $http.post("../itemCat/searchItemCat.do?pageNum=" + currentPage
            + "&pageSize=" + itemsPerPage, searchEntity);
    }
    //读取列表数据绑定到表单中
    this.selectAllItemCats=function(){
        return $http.get('../itemCat/selectAllItemCats.do');
    }
    //分页
    this.findPage = function (page, rows) {
        return $http.get('../itemCat/findPage.do?page=' + page + '&rows=' + rows);
    }
    //查询实体
    this.selectItemCatById = function (id) {
        return $http.get('../itemCat/selectItemCatById.do?id=' + id);
    }
    //增加
    this.insertItemCat = function (entity) {
        return $http.post('../itemCat/insertItemCat.do', entity);
    }
    //修改
    this.updateItemCat = function (entity) {
        return $http.post('../itemCat/updateItemCat.do', entity);
    }
    //删除
    this.dele = function (ids) {
        return $http.get('../itemCat/delete.do?ids=' + ids);
    }
    //搜索
    this.search = function (page, rows, searchEntity) {
        return $http.post('../itemCat/search.do?page=' + page + "&rows=" + rows, searchEntity);
    }

    this.selectItemCatsMapByParentId = function (parentId) {
        return $http.get("../itemCat/selectItemCatsMapByParentId.do?parentId=" + parentId);
    }
});
