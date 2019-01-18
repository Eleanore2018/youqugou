app.controller("itemController", function ($scope, $controller, $location, typeTemplateService, itemCatService, uploadService, itemService) {

    $controller("baseController", {$scope: $scope});//继承




    // 定义选中的id数组
    $scope.selectIds = [];
    $scope.selectId = function (event, id) {
        if (event.target.checked) {
            $scope.selectIds.push(id);
        } else {
            $scope.selectIds.splice($scope.selectIds.indexOf(id), 1);
        }
    }


    $scope.searchEntity = {};//定义搜索对象

    //搜索
    $scope.search = function (currentPage, itemsPerPage) {
        itemService.searchitemPage(currentPage, itemsPerPage, $scope.searchEntity).success(
            function (response) {
                $scope.itemList = response.rows;
                $scope.paginationConf.totalItems = response.total;//更新总记录数
            }
        );
    }




    // 显示状态
    $scope.status = ["未审核", "审核通过", "审核未通过", "关闭"];


});
