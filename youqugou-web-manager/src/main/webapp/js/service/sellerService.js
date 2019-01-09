app.service('sellerService',function($http){
	this.searchSeller = function (currentPage, itemsPerPage, searchEntity) {
		return $http.post("../seller/searchSeller.do?pageNum=" + currentPage
			+ "&pageSize=" + itemsPerPage, searchEntity);
    }

    this.selectSellerById = function (sellerId) {
		return $http.get("../seller/selectSellerById.do?sellerId=" +sellerId);
    }
});
