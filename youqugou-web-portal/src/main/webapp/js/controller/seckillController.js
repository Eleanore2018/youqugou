app.controller("seckillController",function($scope,seckillService){
    $scope.findSeckillList=function () {
        seckillService.findSeckillList().success(function (response) {
            $scope.seckillList=response;
        });
    }
});