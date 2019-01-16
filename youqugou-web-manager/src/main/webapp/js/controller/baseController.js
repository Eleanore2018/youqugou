app.controller('baseController', function ($scope) {
    //分页控件配置 固定格式
    $scope.paginationConf = {
        currentPage: 1,
        totalItems: 0,
        itemsPerPage: 5,
        perPageOptions: [5, 10, 20, 30, 40, 50],
        onChange: function () {
            $scope.reloadList(); // 调用重新加载
        }
    };

    $scope.reloadList = function () {
        // 向服务器发出请求查询页数据
        $scope.search($scope.paginationConf.currentPage, $scope.paginationConf.itemsPerPage);

    }

    //提取json字符串数据中某个属性，返回拼接字符串 逗号分隔
    $scope.jsonToString = function (jsonString, key) {
        var json = JSON.parse(jsonString); // 将json字符串解析成json对象
        var value = "";
        $.each(json, function (index, element) {
            if (index + 1 == json.length){
                value += element[key];
            }else {
                value += element[key] + ",";
            }
        })

        return value;
    }
});