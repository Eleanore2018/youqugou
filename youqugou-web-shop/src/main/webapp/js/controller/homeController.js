/**
 * Author:Mr Liu
 * Date:2018-12-28
 */
app.controller("homeController", function ($scope, $controller, homeService) {
    $controller("baseController", {$scope: $scope});//继承

    // 定义一个存储商家创建时间和当前时间的对象
    $scope.entity = [];
    $scope.searchTime = function () {

        homeService.searchTime().success(function (response) {
            $scope.entity = response;
        })

    };
    // 设置年集合
    $scope.years = [];
    $scope.$watch("entity", function (newValue, oldValue) {
        if ($scope.entity !== false) {
            var oldYear = $scope.entity[0]; // 商家创建时的年份
            var oldMonth = $scope.entity[1]; // 商家创建时的月份
            var oldDate = $scope.entity[2]; // 商家创建时的日期

            // 获取当前时间的年月日
            var nowTime = new Date();
            var nowYear = nowTime.getFullYear(); // 当前时间年份
            var nowMonth = nowTime.getMonth() + 1; // 当前时间月份
            var nowDate = nowTime.getDay(); // 当前时间日期


            for (var i = oldYear; i <= nowYear; i++) {
                $scope.years.push(i);
            }
            $scope.$watch("year", function (newValue, oldValue) {
                $scope.months = [];
                $scope.timeBucket = {};
                if (JSON.stringify(newValue) !== '{}' && newValue != null) {
                    // 如果变化的year值与商家创建年份一致,则只显示有限月份
                    if (newValue === $scope.entity[0]) {
                        if ($scope.entity[0] === nowYear) {
                            for (var i = oldMonth; i <= nowMonth; i++) {
                                $scope.months.push(i);
                            }
                        } else {
                            for (var i = oldMonth; i <= 12; i++) {
                                $scope.months.push(i);
                            }
                        }
                    } else if (newValue === nowYear) {
                        for (var i = 1; i <= nowMonth; i++) {
                            $scope.months.push(i);
                        }
                    } else {
                        for (var i = 1; i <= 12; i++) {
                            $scope.months.push(i);
                        }
                    }
                }
            })
        }
    });

    // 日期不想管了,就都显示一样的吧,么么哒
    $scope.$watch("month", function (newValue, oldValue) {
        $scope.timeBucket = {};
        if (JSON.stringify(newValue) !== '{}' && newValue != null) {
            //得到指定某月的全部天数，可以不用去判断闰年还是平年
            var days = getDaysInMonth($scope.year, $scope.month);

            // 设置时间段
            $scope.timeBuckets = ["1-10", "11-20", "20-" + days];
        }
    });

    $scope.$watch("timeBucket", function (newValue, oldValue) {
        $scope.lineChart = {horizontalAxis: [], verticalAxis: []};
        if (JSON.stringify(newValue) !== '{}' && newValue != null) {
            homeService.getSales($scope.year, $scope.month, $scope.timeBucket).success(function (response) {
                $scope.lineChart.horizontalAxis = response.horizontalAxis;
                $scope.lineChart.verticalAxis = response.verticalAxis;
            })
        }
    });

    $scope.$watch("lineChart.verticalAxis", function (newValue, oldValue) {
        if ($scope.lineChart.verticalAxis !== false) {
            // 指定图表的配置项和数据
            var option = {
                title: {
                    text: 'Sales line chart'
                },
                tooltip: {},
                legend: {
                    data: ['sales']
                },
                xAxis: {
                    name: 'days',
                    data: $scope.lineChart.horizontalAxis
                },
                yAxis: {
                    name: 'daily sales'
                },
                series: [{
                    name: 'sales',
                    type: 'line',
                    data: $scope.lineChart.verticalAxis
                }]
            };

            // 使用刚指定的配置项和数据显示图表。
            myChart.setOption(option);
        }
    })
});

function getDaysInMonth(year, month) {
    month = parseInt(month, 10);
    var temp = new Date(year, month, 0);
    return temp.getDate();
}