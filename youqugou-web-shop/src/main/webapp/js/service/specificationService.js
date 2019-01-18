app.service("specificationService", function ($http) {
    this.searchSpecification = function (currentPage, itemsPerPage, searchEntity) {
        return $http.post("../specification/searchSpecification.do?pageNum=" + currentPage
            + "&pageSize=" + itemsPerPage, searchEntity);
    }

    this.selectSpecificationVoById = function (id) {
        return $http.get("../specification/selectSpecificationVoById.do?id=" + id);
    }

    this.insertSpecificationVo = function (specificationVo) {
        return $http.post("../specification/insertSpecificationVo.do", specificationVo);
    }

    this.updateSpecificationVo = function (specificationVo) {
        return $http.post("../specification/updateSpecificationVo.do", specificationVo);
    }

    this.deleteSpecificationVo = function (ids) {
        return $http.post("../specification/deleteSpecificationVo.do",ids);
    }

    this.selectAllSpecificationMap = function () {
        return $http.get("../specification/selectAllSpecificationMap.do");
    }
});