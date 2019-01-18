app.service("seckillService",function($http){
    this.findSeckillList=function () {
        return $http.get('seckill/findSeckillList.do');
    }

});