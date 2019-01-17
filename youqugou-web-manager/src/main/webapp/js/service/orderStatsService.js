/**
 * Author: Mr Liu
 * Date: 2019/01/08 21:05
 */
app.service("orderStatsService", function ($http) {
    this.getSalesByCategory2Operator = function (year, month, timeBucket) {
        return $http.get("../order/getSalesByCategory2Operator.do?year=" + year + "&month=" + month + "&timeBucket=" +timeBucket);
    }
    this.download = function () {
        return $http.get("../order/downloadOrder.do");
    }
});