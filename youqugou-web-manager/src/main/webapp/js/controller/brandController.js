app.controller("brandController", function ($controller, $scope, brandService) {

    $controller("baseController", {$scope: $scope});

    // 让$scope.searchEntity不为空
    $scope.searchEntity = {};
    // 带查询条件
    $scope.search = function (currentPage, itemsPerPage) {
        brandService.searchBrand(currentPage, itemsPerPage, $scope.searchEntity).success(function (response) {
            $scope.paginationConf.totalItems = response.total;
            $scope.pageBrandList = response.rows;
        })
    }

    // 原始代码
    $scope.selectPageBrand = function (currentPage, itemsPerPage) {
        brandService.selectPageBrand(currentPage, itemsPerPage).success(function (response) {
            $scope.paginationConf.totalItems = response.total;
            $scope.pageBrandList = response.rows;
        })
    }

    // 保存/修改数据
    $scope.save = function () {
        var method;
        // id不为空,则证明是修改数据
        if ($scope.entity.id != null) {
            method = brandService.updateBrand($scope.entity);
        } else {
            method = brandService.insertBrand($scope.entity);
        }
        method.success(function (response) {
            if (response.flag) {
                alert(response.message);
                $scope.reloadList();
            } else {
                alert(response.message);
            }
        })
    }

    // 回显数据,通过id查询entity数据
    $scope.selectBrandById = function (id) {
        brandService.selectBrandById(id).success(function (response) {
            $scope.entity = response;
        })
    }

    // 删除,可以支持批量删除,删除完之后,需要重新刷新页面,然后将原有的ids数据清空
    $scope.delete = function () {
        if (confirm("确认删除吗?")) {
            brandService.deleteBrandByIds($scope.ids).success(function (response) {
                if (response.flag) {
                    alert(response.message);
                    $scope.reloadList();
                    // 初始化ids数组
                    $scope.ids = [];
                }
            });
        }
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
});