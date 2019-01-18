app.service("addressService", function ($http) {

    /**
     * 马超  2019.1.2
     * @param entity
     * @returns {*|void}
     */
    // this.addAddress = function (entity) {
    //     return $http.post("../address/addAddress.do",entity)
    // }
    //增加
    this.add=function(entity){
        return  $http.post('../address/addAddress.do',entity );
    }
    //修改
    this.update=function(entity){
        return  $http.post('../address/update.do',entity );
    }

    this.loginInfo=function () {
        return $http.post("../address/loginInfo.do")
    }
    //删除
    this.deleteAddress=function (id) {
        return $http.post("../address/deleteAddress.do",id)
    }

    // //编辑地址回显
    // this.editBack=function (id) {
    //     return $http.post("../address/editBack.do",id)
    // }

    this.findOne=function (id) {
        return $http.get('../address/findOne.do?id='+id);
    }

  /*  this.findList=function () {
        return $http.get('../address/findList.do');
    }

    this.findList2=function (id) {
        return $http.get('../address/findList2.do?id='+id);
    }

    this.findList3=function (id) {
        return $http.get('../address/findList3.do?id='+id);
    }*/

});