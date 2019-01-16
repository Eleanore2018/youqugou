/**
 * 贾运通2018/12/30
 */
app.controller("brandAuditController", function ($controller, $scope, brandAuditService) {

    $controller("baseController", {$scope: $scope});

    // 让$scope.searchEntity不为空
    $scope.searchEntity = {};
    // 带查询条件
    $scope.search = function (currentPage, itemsPerPage) {
        brandAuditService.searchBrandAudit(currentPage, itemsPerPage, $scope.searchEntity).success(function (response) {
            $scope.paginationConf.totalItems = response.total;
            $scope.pageBrandList = response.rows;
        })
    }


    // 定义ids数组
    $scope.ids = [];
    // 判断复选框状态,选中则往数组添加,取消则清除
    $scope.selectIds = function (event, id) {
        if (event.target.checked) {
            $scope.ids.push(id);
        } else {
            // 根据id值找到索引,可以是数组
            var index = $scope.ids.indexOf(id);
            // 两个参数,第一个是开始索引,第二个是删除几个,因为数组元素可重复
            $scope.ids.splice(index, 1);
        }
    }

    console.log($scope.ids);


    /*贾运通2018/12/28*/
    // 显示状态
    $scope.status = ["未审核", "审核通过", "审核未通过", "关闭"];

    $scope.selectIds = [];
    $scope.updateSelection = function (event, id) {
        if (event.target.checked) {
            $scope.selectIds.push(id);
        } else {
            $scope.selectIds.splice($scope.selectIds.indexOf(id), 1);
        }
    }


    // 审核的方法:
    $scope.updateStatus = function (status) {
        brandAuditService.updateStatus($scope.selectIds, status).success(function (response) {
            if (response.flag) {
                $scope.reloadList();//刷新列表
                $scope.selectIds = [];
            } else {
                alert(response.message);
            }
        });
    }
});