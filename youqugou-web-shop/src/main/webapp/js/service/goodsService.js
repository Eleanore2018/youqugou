//服务层
app.service('goodsService',function($http){

    //读取列表数据绑定到表单中
    this.findAll=function(){
        return $http.get('../goods/findAll.do');
    }
    //分页
    this.findPage=function(page,rows){
        return $http.get('../goods/findPage.do?page='+page+'&rows='+rows);
    }
    //查询实体
    this.selectGoodsVoById=function(id){
        return $http.get('../goods/selectGoodsVoById.do?id='+id);
    }
    //增加
    this.insertGoodsVo=function(entity){
        return  $http.post('../goods/insertGoodsVo.do',entity );
    }
    //修改
    this.updateGoodsVo=function(entity){
        return  $http.post('../goods/updateGoodsVo.do',entity );
    }
    //删除
    this.deleteGoodsVo=function(ids){
        return $http.get('../goods/deleteGoodsVo.do?ids='+ids);
    }
    //搜索
    this.searchGoodsPage=function(currentPage,itemsPerPage,searchEntity){
        return $http.post('../goods/searchGoodsPage.do?pageNum='+currentPage+"&pageSize="+itemsPerPage, searchEntity);
    }
});
