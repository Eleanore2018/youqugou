app.service("typeTemplateService", function ($http) {
    this.searchTypeTemplate = function (currentPage, itemsPerPage, searchEntity) {
        return $http.post("../typeTemplate/searchTypeTemplate.do?pageNum=" + currentPage
            + "&pageSize=" + itemsPerPage, searchEntity);
    }

    this.selectTypeTemplateById = function (id) {
        return $http.get("../typeTemplate/selectTypeTemplateById.do?id=" + id);
    }

    this.updateTypeTemplate = function (typeTemplate) {
        return $http.post("../typeTemplate/updateTypeTemplate.do", typeTemplate);
    }

    this.insertTypeTemplate = function (typeTemplate) {
        return $http.post("../typeTemplate/insertTypeTemplate.do", typeTemplate);
    }

    this.deleteTypeTemplateByIds = function (ids) {
        return $http.post("../typeTemplate/deleteTypeTemplateByIds.do", ids);
    }

    this.selectAllTypeTemplates = function () {
        return $http.get("../typeTemplate/selectAllTypeTemplates.do");
    }

    // 查询所有(id,name as text)用于select2
    this.selectAllTypeTemplatesMap = function () {
        return $http.get("../typeTemplate/selectAllTypeTemplatesMap.do");
    }
});