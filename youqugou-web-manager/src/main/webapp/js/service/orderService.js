app.service('orderService',function($http){
    //分页查询订单  张静 2018-12-30
    this.searchOrder = function (currentPage, itemsPerPage, searchEntity) {
        return $http.post("../order/searchOrder.do?pageNum=" + currentPage
            + "&pageSize=" + itemsPerPage, searchEntity);
    }

    //查询指定id的订单  张静 2018-12-30
    this.selectOrderById=function (id) {
        return $http.get("../order/selectOrderById.do?id="+id);
    }

    //查询指定id的订单详情  张静 2018-12-30
    this.selectOrderItemById=function (id) {
        return $http.get('../order/selectOrderItemById.do?id='+id);
    }
});