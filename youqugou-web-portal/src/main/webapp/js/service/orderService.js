app.service('orderService',function($http){
    this.search=function(s,pageSize,pageNo){
        return $http.get('order/search.do?pageNo='+pageNo+"&pageSize="+pageSize+"&status="+s);
    }


    //查询指定id的订单详情  张静
    this.selectOrderItemById=function (id) {
        return $http.get('order/selectOrderItemById.do?id='+id);
    }

    //去支付 张静
    this.toPay=function (id) {
        return $http.get('order/toPay.do?id='+id);
    }
});