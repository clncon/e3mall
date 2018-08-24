<html>
<head>
<title>student</title>
</head>
<body>
   学生信息:<br>
   学号:${student.id}&nbsp;&nbsp;&nbsp;&nbsp;
   姓名:${student.name}&nbsp;&nbsp;&nbsp;&nbsp;
   年龄:${student.age}&nbsp;&nbsp;&nbsp;&nbsp;
   住址:${student.address}<br>
   
   学生列表：
   <table border="1">
     <tr>
       <th>学号</th>
       <th>姓名</th>
       <th>年龄</th>
       <th>家庭住址</th>
     </tr>
     <#list studentList as student>
     <#if student_index %2 ==0>
      <tr bgcolor="red">
      <#else>
        <tr bgcolor="green">
       </#if>
        <td>${student_index}</td>
        <td>${student.id}</td>
        <td>${student.name}</td>
        <td>${student.age}</td>
        <td>${student.address}</td>
      </tr>
      </#list>
   </table>
   <br>
   当前日期:${date?string("yyyy/MM/dd HH:mm:ss")}
   null值的处理
    ${val!"val的值为空"}<br/>
    null值的判断
    <#if val??>
     val的值不为空
     <#else>
     val的的值为空
     </#if>
   引用模板测试：<br/>
   <#include "hello.ftl">
</body>
</html>