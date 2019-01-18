/**
 * Author: Mr Liu
 * Date: 2019/1/2 00:24
 */
app.service("orderStatsService", function ($http) {
    this.getSalesByCategory = function (year, month, timeBucket) {
        return $http.get("../order/getSalesByCategory.do?year=" + year + "&month=" + month + "&timeBucket=" +timeBucket);
    }
    this.download = function () {
        return $http.get("../order/downloadOrder.do");
    }
});