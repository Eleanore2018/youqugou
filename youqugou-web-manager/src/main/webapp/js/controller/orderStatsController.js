/**
 * Author: Mr Liu
 * Date: 2019/01/08 21:05
 */
app.controller('orderStatsController', function ($scope, orderStatsService, homeService) {
    orderStatsService.download().success(function (response) {

    });

    // 定义一个存储优趣购上线的时间
    $scope.entity = [2016, 9, 30];
    $scope.$watch("entity", function (newValue, oldValue) {
        if (newValue !== false) {
            var oldYear = $scope.entity[0]; // 优趣购上线的年份
            var oldMonth = $scope.entity[1]; // 优趣购上线的月份
            var oldDate = $scope.entity[2]; // 优趣购上线的日期

            // 获取当前时间的年月日
            var nowTime = new Date();
            var nowYear = nowTime.getFullYear(); // 当前时间年份
            var nowMonth = nowTime.getMonth() + 1; // 当前时间月份(getMonth获取的是从0开始到11的月份)
            var nowDate = nowTime.getDay(); // 当前时间日期

            // 设置年集合
            $scope.years = [];
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
            });
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
        // 销量折线图
        $scope.pieChart = [];
        if (JSON.stringify(newValue) !== '{}' && newValue != null) {
            orderStatsService.getSalesByCategory2Operator($scope.year, $scope.month, $scope.timeBucket).success(function (response) {
                $scope.pieChart = response;
            })
        }
    });

    $scope.$watch("pieChart", function (newValue, oldValue) {
        // 判断数组不为空
        if (newValue !== false) {
            var data = genData();
            var pieOption = {
                title: {
                    text: '商品销售额饼状图',
                    left: 'center',
                    top: 20,
                    textStyle: {
                        color: '#000'
                    }
                },
                tooltip: {
                    trigger: 'item',
                    formatter: "{a} <br/>{b}: {c} ({d}%)"
                },
                legend: {
                    orient: 'vertical',
                    x: 'left',
                    data:data.legendData
                },
                series: [
                    {
                        name:'商品销售额',
                        type:'pie',
                        radius: ['50%', '70%'],
                        avoidLabelOverlap: false,
                        label: {
                            normal: {
                                show: false,
                                position: 'center'
                            },
                            emphasis: {
                                show: true,
                                textStyle: {
                                    fontSize: '30',
                                    fontWeight: 'bold'
                                }
                            }
                        },
                        labelLine: {
                            normal: {
                                show: false
                            }
                        },
                        data:data.seriesData
                    }
                ]
            };
        }
        function genData() {
            var seriesData = [];
            var legendData = [];
            for (var i = 0; i < $scope.pieChart.length; i++) {
                seriesData.push({
                    name: $scope.pieChart[i].name,
                    value: $scope.pieChart[i].value
                });
                legendData.push({
                    name : $scope.pieChart[i].name
                })
            }
            return {
                seriesData : seriesData,
                legendData : legendData
            };
        }
        pieChart.setOption(pieOption);
    });
});

function getDaysInMonth(year, month) {
    month = parseInt(month, 10);
    var temp = new Date(year, month, 0);
    return temp.getDate();
}

// 自定义排序方式,根据数组中对象的某个属性进行排序,此处为降序
function compare(property) {
    return function (a, b) {
        return b[property] - a[property];
    }
}