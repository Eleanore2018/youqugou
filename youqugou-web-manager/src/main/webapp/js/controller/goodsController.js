//控制层
app.controller('goodsController', function ($scope, $controller, itemCatService, goodsService) {

    $controller('baseController', {$scope: $scope});//继承

    //读取列表数据绑定到表单中
    $scope.findAll = function () {
        goodsService.findAll().success(
            function (response) {
                $scope.list = response;
            }
        );
    }

    //分页
    $scope.findPage = function (page, rows) {
        goodsService.findPage(page, rows).success(
            function (response) {
                $scope.list = response.rows;
                $scope.paginationConf.totalItems = response.total;//更新总记录数
            }
        );
    }

    //查询实体
    $scope.findOne = function (id) {
        goodsService.findOne(id).success(
            function (response) {
                $scope.entity = response;
            }
        );
    }

    //保存
    $scope.save = function () {
        var serviceObject;//服务层对象
        if ($scope.entity.id != null) {//如果有ID
            serviceObject = goodsService.update($scope.entity); //修改
        } else {
            serviceObject = goodsService.add($scope.entity);//增加
        }
        serviceObject.success(
            function (response) {
                if (response.flag) {
                    //重新查询
                    $scope.reloadList();//重新加载
                } else {
                    alert(response.message);
                }
            }
        );
    }


    //批量删除
    $scope.delete = function () {
        //获取选中的复选框
        goodsService.delete($scope.selectIds).success(
            function (response) {
                if (response.flag) {
                    $scope.reloadList();//刷新列表
                    $scope.selectIds = [];
                }
            }
        );
    }

    $scope.searchEntity = {};//定义搜索对象

    //搜索
    $scope.search = function (currentPage, itemsPerPage) {
        goodsService.searchGoodsPage(currentPage, itemsPerPage, $scope.searchEntity).success(
            function (response) {
                $scope.list = response.rows;
                $scope.paginationConf.totalItems = response.total;//更新总记录数
            }
        );
    }

    // 显示状态
    $scope.status = ["未审核", "审核通过", "审核未通过", "关闭"];

    $scope.itemCatList = [];
    // 显示分类:
    $scope.selectAllItemCats = function () {
        itemCatService.selectAllItemCats().success(function (response) {
            for (var i = 0; i < response.length; i++) {
                $scope.itemCatList[response[i].id] = response[i].name;
            }
        });
    }

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
        goodsService.updateStatus($scope.selectIds, status).success(function (response) {
            if (response.flag) {
                $scope.reloadList();//刷新列表
                $scope.selectIds = [];
            } else {
                alert(response.message);
            }
        });
    }
});
