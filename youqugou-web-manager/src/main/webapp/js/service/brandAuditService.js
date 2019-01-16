/**
 * 贾运通2018/12/30
 */

app.service("brandAuditService", function ($http) {
    this.searchBrandAudit = function (currentPage, itemsPerPage,searchEntity) {
        return $http.post("../brandAudit/searchBrandAudit.do?pageNum=" + currentPage
            + "&pageSize=" + itemsPerPage, searchEntity);
    }


    this.updateStatus = function (ids,status) {
        return $http.get('../brandAudit/updateStatus.do?ids='+ids+"&status="+status);
    }


});