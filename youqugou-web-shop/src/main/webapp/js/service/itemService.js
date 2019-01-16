//服务层
app.service('itemService',function($http){


    //搜索
    this.searchitemPage=function(currentPage,itemsPerPage,searchEntity){
        return $http.post('../item/searchitemPage.do?pageNum='+currentPage+"&pageSize="+itemsPerPage, searchEntity);
    }
});
