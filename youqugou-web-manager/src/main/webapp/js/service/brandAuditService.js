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

    this.selectBrandById = function (id) {
        return $http.get("../brand/selectBrandById.do?id=" + id)
    }

    this.deleteBrandByIds = function (ids) {
        return $http.post("../brandAudit/deleteBrandByIds.do", ids)
    }

    this.selectAllBrandMap = function () {
        return $http.get("../brand/selectAllBrandMap.do");
    }
});