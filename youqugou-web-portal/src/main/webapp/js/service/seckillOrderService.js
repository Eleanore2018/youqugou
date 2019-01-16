app.service('seckillOrderService',function($http){
    //分页查询订单  张静 2019-01-01
    this.search = function (currentPage, itemsPerPage) {
        return $http.get("../seckillOrder/search.do?pageNum=" + currentPage
            + "&pageSize=" + itemsPerPage);
    }

    //根据id取消订单  张静 2019-01-01
    this.cancelOrderById=function (id) {
        return $http.get("../seckillOrder/cancelOrderById.do?id="+id);
    }
});