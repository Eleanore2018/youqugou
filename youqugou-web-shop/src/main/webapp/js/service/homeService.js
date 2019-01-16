/**
 * Author:Mr Liu
 * Date:2018-12-28
 */
app.service("homeService", function ($http) {
    this.searchTime = function () {
        return $http.get("../home/searchTime.do");
    }
    this.getSales = function (year, month, timeBucket) {
        return $http.get("../home/getSales.do?year=" + year + "&month=" + month + "&timeBucket=" +timeBucket);
    }
});