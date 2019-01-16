//服务层
app.service('itemService',function($http){
    //张静 2019-01-02 根据商品id查询库存
    this.findItemMapByGoodsId=function (id) {
        return $http.get("../item/findItemMapByGoodsId.do?id="+id);
    }
});