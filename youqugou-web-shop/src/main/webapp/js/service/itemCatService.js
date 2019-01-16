//服务层
app.service('itemCatService',function($http){

    //读取列表数据绑定到表单中
    this.selectAllItemCats=function(){
        return $http.get('../itemCat/selectAllItemCats.do');
    }
    //分页
    this.findPage=function(page,rows){
        return $http.get('../itemCat/findPage.do?page='+page+'&rows='+rows);
    }
    //查询实体
    this.selectItemCatById=function(id){
        return $http.get('../itemCat/selectItemCatById.do?id='+id);
    }
    //增加
    this.add=function(entity){
        return  $http.post('../itemCat/add.do',entity );
    }
    //修改
    this.update=function(entity){
        return  $http.post('../itemCat/update.do',entity );
    }
    //删除
    this.dele=function(ids){
        return $http.get('../itemCat/delete.do?ids='+ids);
    }
    //搜索
    this.search=function(page,rows,searchEntity){
        return $http.post('../itemCat/search.do?page='+page+"&rows="+rows, searchEntity);
    }

    this.selectItemCatsByParentId = function(parentId){
        return $http.get("../itemCat/selectItemCatsByParentId.do?parentId="+parentId);
    }


});