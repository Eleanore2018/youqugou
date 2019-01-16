app.service("contentService",function($http){
	this.selectContentsByCategoryId = function(categoryId){
		return $http.get("content/selectContentsByCategoryId.do?categoryId="+categoryId);
	}
});