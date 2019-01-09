/**
 * Author:Mr Liu
 * Date:2018-12-30
 */
app.controller("homeController", function ($scope, $controller, homeService) {

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
        $scope.chart = {lineChart:{horizontalAxis:[],verticalAxis:[]},cookieChart:{sellerNames:[],sellerSales:[]}};
        if (JSON.stringify(newValue) !== '{}' && newValue != null) {
            homeService.getSales($scope.year, $scope.month, $scope.timeBucket).success(function (response) {
                $scope.chart.lineChart.horizontalAxis = response.lineChart.horizontalAxis;
                $scope.chart.lineChart.verticalAxis = response.lineChart.verticalAxis;
                $scope.chart.cookieChart.sellerNames = response.cookieChart.sellerNames;
                $scope.chart.cookieChart.sellerSales = response.cookieChart.sellerSales;
            })
        }
    });
    $scope.$watch("chart.lineChart.verticalAxis", function (newValue, oldValue) {
        // 判断数组不为空
        if (newValue !== false) {
            // 指定图表的配置项和数据
            var lineOption = {
                title: {
                    text: 'Sales line chart'
                },
                tooltip: {},
                legend: {
                    data: ['sales']
                },
                xAxis: {
                    name: 'days',
                    data: $scope.chart.lineChart.horizontalAxis
                },
                yAxis: {
                    name: 'daily sales'
                },
                series: [{
                    name: 'sales',
                    type: 'line',
                    data: $scope.chart.lineChart.verticalAxis
                }]
            };

            // 使用刚指定的配置项和数据显示图表。
            lineChart.setOption(lineOption);
        }
    });

    $scope.$watch("chart.cookieChart.sellerSales", function (newValue, oldValue) {
        // 判断数组不为空
        if (newValue !== false) {
            // 销量饼状图
            var data = genData();

            var cookieOption = {
                title: {
                    text: 'Pie chart of business sales',
                    x: 'left'
                },
                tooltip: {
                    trigger: 'item',
                    formatter: "{a} <br/>{b} : {c} ({d}%)"
                },
                legend: {
                    type: 'scroll',
                    orient: 'vertical',
                    right: 10,
                    top: 20,
                    bottom: 20,
                    data: data.legendData,
                    selected: data.selected
                },
                series: [
                    {
                        name: '姓名',
                        type: 'pie',
                        radius: '55%',
                        center: ['40%', '50%'],
                        data: data.seriesData,
                        itemStyle: {
                            emphasis: {
                                shadowBlur: 10,
                                shadowOffsetX: 0,
                                shadowColor: 'rgba(0, 0, 0, 0.5)'
                            }
                        }
                    }
                ]
            };
            function genData() {
                // 饼状图右侧显示的数据
                var legendData = [];
                // 饼状图显示的数据
                var seriesData = [];
                // 表示选中的条数,里面存储的是name
                var selected = {};
                for (var i = 0; i < $scope.chart.cookieChart.sellerSales.length; i++) {
                    seriesData.push({
                        name: $scope.chart.cookieChart.sellerNames[i],
                        value: $scope.chart.cookieChart.sellerSales[i]
                    });
                }
                // 调用排序方式进行排序,根据value属性排序
                seriesData.sort(compare('value'));

                // 排序后重新遍历,给饼状图属性赋值
                for (var i = 0; i < seriesData.length; i++) {
                    legendData.push(seriesData[i].name);
                    selected[seriesData[i].name] = i < 6;
                }
                return {
                    legendData: legendData,
                    seriesData: seriesData,
                    selected: selected
                };
            }
        }
        // 使用刚指定的配置项和数据显示图表。
        cookieChart.setOption(cookieOption);
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