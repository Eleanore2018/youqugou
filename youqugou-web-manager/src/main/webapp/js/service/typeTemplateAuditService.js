/**
 * 贾运通 2018/12/31
 */
app.service("typeTemplateAuditService", function ($http) {
    this.searchTypeTemplateAudit = function (currentPage, itemsPerPage, searchEntity) {
        return $http.post("../typeTemplateAudit/searchTypeTemplateAudit.do?pageNum=" + currentPage
            + "&pageSize=" + itemsPerPage, searchEntity);
    }


    //状态修改【审核】
    this.updateStatus = function (ids,status) {
        return $http.get('../typeTemplateAudit/updateStatus.do?ids='+ids+"&status="+status);
    }

});