app.controller("sellerController", function ($controller, $scope, sellerService) {
    $controller("baseController", {$scope:$scope});

    // 注册商家
    $scope.insertSeller = function () {
        sellerService.insertSeller($scope.entity).success(function (response) {
            if (response.flag) {
                // 重新查询,等待通知(是否入驻成功)
                // $scope.reloadList();
                // 注册成功,则跳转至登录页
                window.location.href = "shoplogin.html";
            } else {
                alert(response.message);
            }
        })
    }
});