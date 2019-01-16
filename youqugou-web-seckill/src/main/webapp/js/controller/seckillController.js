app.controller("seckillController",function($scope,$location,$interval,seckillService){

    //查询当前秒杀的商品列表 张静 2018-12-31
    $scope.findSeckillList=function () {
        seckillService.findSeckillList().success(function (response) {
            $scope.seckillList=response;
        });
    }

    //显示指定的商品  张静 2018-12-31
    $scope.findOne=function () {
        var id = $location.search()['id'];
        //alert(id);
        seckillService.findOne(id).success(function (response) {
            $scope.entity=response;
            var endtime= new Date($scope.entity.endTime).getTime();
            var curTime=new Date().getTime();
            $scope.totalSecond=Math.round((endtime-curTime)/1000);
            //实现当前时间倒计时
            timePromise=$interval(function () {
                if ($scope.totalSecond>=0){
                    var t1=Math.floor($scope.totalSecond/60);
                    var m=t1<10?"0"+t1:t1;
                    var t2=$scope.totalSecond-t1*60;
                    var s=t2<10?"0"+t2:t2;
                    $scope.totalSecond=$scope.totalSecond-1;
                    $scope.CountDown=m+":"+s;
                }else{
                    $interval.cancel(timePromise);
                }
            },1000);
        })



        //张静 2019-01-01 秒杀
        $scope.goToSeckill=function (id) {
            $scope.id=id;
            seckillService.goToSeckill(id).success(function (response) {
                if (response.flag){
                    alert(response.message);
                }else{
                    alert(response.message);
                    location.href="http://localhost:9105/seckill-index.html";
                }
            })
        }

       /* //获取秒杀到的商品生成订单 张静 2019-01-01
        $scope.findOrderById=function () {
            alert($scope.id);
            seckillService.findOrderById($scope.id).success(function (response) {
                $scope.entityOrder=response;
            })
        }*/
    }

});