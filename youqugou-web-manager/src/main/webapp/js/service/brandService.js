app.service("brandService", function ($http) {
    this.searchBrand = function (currentPage, itemsPerPage,searchEntity) {
        return $http.post("../brand/searchBrand.do?pageNum=" + currentPage
            + "&pageSize=" + itemsPerPage, searchEntity);
    }

    this.selectPageBrand = function (currentPage, itemsPerPage) {
        return $http.get("../brand/selectPageBrand.do?pageNum=" + currentPage
            + "&pageSize=" + itemsPerPage)
    }

    this.insertBrand = function (entity) {
        return $http.post("../brand/insertBrand.do", entity)
    }

    this.updateBrand = function (entity) {
        return $http.post("../brand/updateBrand.do", entity)
    }

    this.selectBrandById = function (id) {
        return $http.get("../brand/selectBrandById.do?id=" + id)
    }

    this.deleteBrandByIds = function (ids) {
        return $http.post("../brand/deleteBrandByIds.do", ids)
    }

    this.selectAllBrandMap = function () {
        return $http.get("../brand/selectAllBrandMap.do");
    }

    this.importExcle = function () {
        return $http.get("../brand/importExcle.do?filePath=C:\\Users\\WayShinechane\\Desktop\\brand.xls");
    /*贾运通2018/12/28*/
    //审核
    this.updateStatus = function(ids,status){
        return $http.get('../brand/updateStatus.do?ids='+ids+"&status="+status);
    }
});