$(function() {
    //给登录按钮绑定点击事件，校验表单，如果通过，发送ajax进行登录操作，登录成功后跳转首页
    $("#loginBtn").click(function(){
        //校验表单另一个第三方
        if( $("#customerForm").validate().form() ){
            $.ajax({
                "url" : "/customer/login" ,
                "type" : "get" ,
                "data" : $("#customerForm").serializeJson() ,
                "statusCode" : {
                    200:function(data){
                        //登录成功后跳转首页
                        $.messager.alert("提示",data,"info",function(){
                            location.href = "/index";
                        });
                    },
                    500 : function(data){
                        $.messager.alert("提示",data.responseText,"warning");
                        //切换验证码 (触发图片的点击事件)
                        $("#vcode").click();
                    }
                } ,
            });
        }
    });
});

