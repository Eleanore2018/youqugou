app.service("contentService",function($http){
	this.selectContentsByCategoryId = function(categoryId){
		return $http.get("content/selectContentsByCategoryId.do?categoryId="+categoryId);
	}

	this.findFloorContent = function (contentCategory) {
		return $http.post("content/findFloorContent.do",contentCategory);
    }

    this.findAllItemCat = function (parentId) {
        return $http.get("itemCat/findAllItemCat.do?parentId=" + parentId);
    }
});