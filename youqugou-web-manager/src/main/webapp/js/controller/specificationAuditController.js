/**
 * 贾运通2018/12/31
 */
app.controller("specificationAuditController", function ($controller, $scope, specificationAuditService) {

    // 继承baseController
    $controller("baseController", {$scope: $scope});

    // 给定初始值,防止$scope.searchEntity为null
    $scope.searchEntity = {};
    $scope.search = function (currentPage, itemsPerPage) {
        // 查询分页列表 带条件(模糊查询)
        specificationService.searchSpecificationAudit(currentPage, itemsPerPage, $scope.searchEntity).success(function (response) {
            $scope.paginationConf.totalItems = response.total;
            $scope.pageSpecificationList = response.rows;
        })
    }







    // 定义数组,使用户一次可以操作多个对象
    $scope.ids = [];
    $scope.selectIds = function (event, id) {
        if (event.target.checked) {
            $scope.ids.push(id);
        } else {
            var index = $scope.ids.indexOf(id);
            $scope.ids.splice(index,1);
        }
    }

    // 根据ids数组删除数据
    $scope.deleteSpecificationVo = function () {
        if (confirm("是否确认删除?")){
            specificationService.deleteSpecificationVo($scope.ids).success(function (response) {
                if (response.flag) {
                    $scope.reloadList();
                } else {
                    alert(response.message);
                }
            })
        }
    }
});