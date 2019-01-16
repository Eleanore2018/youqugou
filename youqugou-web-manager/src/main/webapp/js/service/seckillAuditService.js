/**
 * 贾运通--2018/12/31
 */
app.service("seckillAuditService", function ($http) {
    this.searchSecKillAudit = function (currentPage, itemsPerPage, searchEntity) {
        return $http.post("../seckillAudit/searchSecKillAudit.do?pageNum=" + currentPage
            + "&pageSize=" + itemsPerPage, searchEntity);
    }

    //状态修改【审核】
    this.updateStatus = function (ids,status) {
        return $http.get('../seckillAudit/updateStatus.do?ids='+ids+"&status="+status);
    }


});