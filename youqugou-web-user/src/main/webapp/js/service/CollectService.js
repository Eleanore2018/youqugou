//服务层
//作者:左建洲
app.service('CollectService',function($http){

	this.selectColl=function(){
		return $http.get('../collect/selectCollect.do');
	}
	
});