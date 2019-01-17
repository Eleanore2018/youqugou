app.service("seckillService",function($http){

    //查询秒杀商品列表 张静 2018-12-31
    this.findSeckillList=function () {
        return $http.get('seckillGoods/findSeckillList.do');
    }



    //查询指定的商品 张静 2018-12-31
    this.findOne=function (id) {
        return $http.get('seckillGoods/findOne.do?id='+id);
    }

    //秒杀  张静 2019-01-01
    this.goToSeckill=function (id) {
        return $http.get('seckillGoods/goToSeckill.do?id='+id);
    }

    /*//查找订单信息包装对象 张静 2019-01-01
    this.findOrderById=function (id) {
        return $http.get('seckilOrder/findOrderById.do?id='+id);
    }*/
});