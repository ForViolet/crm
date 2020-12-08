<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<% String basePath = request.getScheme()
        + "://" + request.getServerName()
        + ":" + request.getServerPort()
        + request.getContextPath() + "/";
%>
<html>
<head>
    <base href="<%=basePath%>">
    <title>Title</title>
</head>
<body>


        //日期控件
        $(".time").datetimepicker({
        minView: "month",
        language:  'zh-CN',
        format: 'yyyy-mm-dd',
        autoclose: true,
        todayBtn: true,
        pickerPosition: "bottom-left"
        });


        String createTime = DateTimeUtil.getSysTime();
        String createBy = ((User)request.getSession().getAttribute("user")).getName();

        $.ajax({
            url:"",
            data:{},
            type:"",
            dataType:"josn",
            success:function(data){

            }
        })



</body>
</html>
