//控制器
//作者:左建洲
app.controller('CollectController', function ($scope, CollectService) {
    $scope.collectList = [];
    $scope.selectColl = function () {
        CollectService.selectColl().success(
            function (response) {
                $scope.collectList = response;
            }
        );
    }
});