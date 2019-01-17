app.service("specificationAuditService", function ($http) {
    this.searchSpecification = function (currentPage, itemsPerPage, searchEntity) {
        return $http.post("../specificationAudit/searchSpecificationAudit.do?pageNum=" + currentPage
            + "&pageSize=" + itemsPerPage, searchEntity);
    }


    this.deleteSpecificationVo = function (ids) {
        return $http.post("../specification/deleteSpecificationVo.do",ids);
    }

    this.selectAllSpecificationMap = function () {
        return $http.get("../specification/selectAllSpecificationMap.do");
    }
});