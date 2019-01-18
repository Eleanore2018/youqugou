app.controller("specificationController", function ($controller, $scope, specificationService) {

    // 继承baseController
    $controller("baseController", {$scope: $scope});

    // 显示状态
    $scope.status = ["未审核", "审核通过", "审核未通过", "关闭"];


    // 给定初始值,防止$scope.searchEntity为null
    $scope.searchEntity = {};
    $scope.search = function (currentPage, itemsPerPage) {
        // 查询分页列表 带条件(模糊查询)
        specificationService.searchSpecification(currentPage, itemsPerPage, $scope.searchEntity).success(function (response) {
            $scope.paginationConf.totalItems = response.total;
            $scope.pageSpecificationList = response.rows;
        })
    }

    // 通过specificationId回显SpecificationVo数据
    $scope.selectSpecificationVoById = function (id) {
        specificationService.selectSpecificationVoById(id).success(function (response) {
            $scope.entity = response;
        })
    }

    // 增加选项
    $scope.addOption = function () {
        $scope.entity.specificationOptions.push({});
    }

    // 删除选项
    $scope.deleteOption = function (index) {
        $scope.entity.specificationOptions.splice(index, 1);
    }

    // 根据条件判断执行修改或新增操作
    $scope.save = function () {
        var method;
        if ($scope.entity.specification.id != null) {
            method = specificationService.updateSpecificationVo($scope.entity);
        } else {
            method = specificationService.insertSpecificationVo($scope.entity);
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