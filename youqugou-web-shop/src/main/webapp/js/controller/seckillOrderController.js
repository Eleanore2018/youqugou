app.controller('seckillOrderController' ,function($scope, $controller, $location,seckillOrderService){

    $controller('baseController',{$scope:$scope});//继承

    //订单状态 1、未付款，2、已付款，3、未发货，4、已发货，5、交易成功，6、交易关闭,7、待评价
    // 张静 2019-01-01
    $scope.status = ["","未支付", "已支付", "未发货", "已发货","交易成功","交易关闭","待评价"];
    $scope.searchEntity = {};
    $scope.search = function (currentPage, itemsPerPage) {
        // console.log($("input[type='radio']:checked").val());
        seckillOrderService.searchOrder(currentPage, itemsPerPage, $scope.searchEntity).success(
            function (response) {
            $scope.paginationConf.totalItems = response.total;
            $scope.orderList = response.rows;
        })
    }

    //根据订单id,查询指定订单  张静 2019-01-01
    $scope.selectOrderById = function (id) {
        seckillOrderService.selectOrderById(id).success(function (response) {
            $scope.entity1 = response;
        })
    }

   /* //根据订单id,查询指定订单详情  张静 2018-12-30
    $scope.selectOrderItemById = function(){
        var id = $location.search()['id'];
        orderService.selectOrderItemById(id).success(
            function (response) {
                $scope.entity2=response;
            }
        )
    }*/
});
