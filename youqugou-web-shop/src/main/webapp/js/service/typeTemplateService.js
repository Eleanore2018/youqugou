//服务层
app.service('typeTemplateService',function($http){

    //读取列表数据绑定到表单中
    this.findAll=function(){
        return $http.get('../typeTemplate/findAll.do');
    }
    //分页
    this.findPage=function(page,rows){
        return $http.get('../typeTemplate/findPage.do?page='+page+'&rows='+rows);
    }
    //查询实体
    this.selectTypeTemplateById = function (id) {
        return $http.get("../typeTemplate/selectTypeTemplateById.do?id=" + id);
    }
    //增加
    this.add=function(entity){
        return  $http.post('../typeTemplate/add.do',entity );
    }
    //修改
    this.update=function(entity){
        return  $http.post('../typeTemplate/update.do',entity );
    }
    //删除
    this.dele=function(ids){
        return $http.get('../typeTemplate/delete.do?ids='+ids);
    }
    //搜索
    this.search=function(page,rows,searchEntity){
        return $http.post('../typeTemplate/search.do?page='+page+"&rows="+rows, searchEntity);
    }

    this.selectSpecListById=function(id){
        return $http.get('../typeTemplate/selectSpecListById.do?id='+id);
    }
});
