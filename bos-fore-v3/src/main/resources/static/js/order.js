$(function() {
    //绑定按钮绑定点击事件，表单校验通过后，发送ajax请求
    $("#saveBtn").click(function(event){
        event.preventDefault();
        if($("#orderForm").validate().form()){
            $.ajax({
                url:'/foreOrder',
                method:'post',
                data:$("#orderForm").serializeJson(),
                statusCode : {
                    200 : function(data){
                        $.messager.alert("提示",data,"info",function(){
                            window.location.href="./index.html";
                        });
                    },
                    400 : function(data){
                        $.messager.alert('提示','参数有误!' + data , "warning");
                    },
                    500 : function(data){
                        $.messager.alert("提示", data.responseText,"warning");
                    }
                }
            });
        }
    })
});
