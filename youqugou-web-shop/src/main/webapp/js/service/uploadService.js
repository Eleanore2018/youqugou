app.service("uploadService", function ($http) {
    this.uploadFile = function () {
        // new一个formData对象,此对象相当于之前的form表单提交,但是此处需要异步提交,不能使用form
        var formData = new FormData();
        formData.append("file", file.files[0]);
        return $http({
            method:'post',
            url:'../upload/uploadFile.do', // url路径
            data:formData, // 上传的文件数据
            headers:{'Content-Type':undefined}, // 传文件需要将请求头修改为undefined
            transformRequest: angular.identity // 使用angularJs的方式上传文件
        });
    }
});