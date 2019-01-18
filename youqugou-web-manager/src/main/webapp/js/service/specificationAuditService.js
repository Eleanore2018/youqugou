/**
 * 贾运通--2018/12/31
 */
app.service("specificationAuditService", function ($http) {
    this.searchSpecificationAudit = function (currentPage, itemsPerPage, searchEntity) {
        return $http.post("../specificationAudit/searchSpecificationAudit.do?pageNum=" + currentPage
            + "&pageSize=" + itemsPerPage, searchEntity);
    }

    //状态修改【审核】
    this.updateStatus = function (ids,status) {
        return $http.get('../specificationAudit/updateStatus.do?ids='+ids+"&status="+status);
    }


});