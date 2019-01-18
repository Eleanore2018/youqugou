app.controller("userController", function ($scope,$controller,  userService) {

    $controller("baseController", {$scope: $scope});

    $scope.search = function (currentPage, itemsPerPage) {
        userService.searchUser(currentPage,itemsPerPage) .success(function (response) {
            $scope.paginationConf.totalItems = response.total;
            $scope.userList = response.rows;
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

    $scope.freeze = function () {
        if (confirm("确定要冻结该用户")) {
            userService.freeze($scope.ids).success(function (response) {
                if (response.flag) {
                    $scope.reloadList();
                    // 初始化ids数组
                    $scope.ids = [];
                } else {
                    alert(response.message);
                }
            });
        }
    }

//    时时统计用户数量
    $scope.userCount = function () {
        userService.userCount().success(function (response) {
            $scope.allUser = response;
            $scope.reloadList();
        })
    }
});