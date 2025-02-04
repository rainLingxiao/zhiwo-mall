<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %> 
<c:set var="ctx" value="${pageContext.request.contextPath}"/>
<%@taglib prefix="shiro" uri="http://shiro.apache.org/tags" %> 
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
	<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
 	<title>富卫运维大平台</title>
</head>
<body>
<link rel="stylesheet" href="${ctx}/resources/libs/ztree/zTreeStyle.css" type="text/css">
	<link rel="stylesheet" type="text/css" href="${ctx}/resources/css/style.css">
<script type="text/javascript" src="${ctx}/resources/js/common/jquery-3.2.1.min.js"></script>
<script type="text/javascript"	src="${ctx}/resources/libs/layer/layer.js"></script>
<script type="text/javascript" src="${ctx}/resources/libs/datepicker/WdatePicker.js"></script>
<script type="text/javascript" src="${ctx}/resources/js/common/page.js"></script>
<%--  <script src="${ctx}/resources/js/common/jquery-3.2.1.min.js"></script>  --%>
	<div class="bg hide"></div>
	<div class="popup hide">
		<div class="tb_hd">
			<!-- <span class="table_header">选择讲师</span> -->
			<div class="form-search">
				<input type="text" placeholder="" class="form-control">
				<i><img src="../../imgs/search.png" alt=""></i>
			</div>
			<button class="cancel_tb"><img src="../../imgs/cancel.png" alt=""></button>
		</div>
		<table class="table table-agents">
			<thead>
				<tr>
					<th>
						<label class="pos-rel">
							<input type="checkbox" class="ace"  id="checkbox" >
						</label>
					</th>
					<th>编号</th>
					<th>讲师姓名</th>
					<th>手机号</th>
					<th>邮箱</th>
					<th>职级</th>
				</tr>
			</thead>
			<tbody>
				<c:if test="${not empty accountList}">
					<c:forEach  varStatus="idx" var="account" items="${accountList}">
						<tr>
							<td>
								<input id="accountId" name="checkbox" type="checkbox" class="ace" value="${account.id }">
							</td>
							<td>${account.id }</td>
							<td><a  href="#" class="clickable">${account.accountName }</a></td>
							<td>${account.mobile }</td>
							<td>${account.email }</td>
							<td>
								<c:if test="${account.postType==1 }">总监</c:if>
								<c:if test="${account.postType==2 }">主管</c:if>
								<c:if test="${account.postType==3 }">代理人</c:if>
								<c:if test="${account.postType==4 }">内勤</c:if>
							</td>
						</tr>
					</c:forEach>
				</c:if>
			</tbody>
		</table>
		<div class="btn_list" align="center" style="margin-top: 15px;">
			<button class="btn btn-submit" onclick="addLecturers()">确定</button>
			<button class="btn btn-default">取消</button>
		</div>
		<input type="hidden" class="ace"  id="courseId" value="${course.id }" >
	</div>
	<script type="text/javascript">
	function addLecturers() {
		/* alert("stu"); */
		var id= $("#courseId").val();
		var jsonStr =[];
		 var accountIds = new Array();
		 var accountNames = new Array();
		 var groupCheckbox=$("input[name='checkbox']:checked");
		     for(i=0;i<groupCheckbox.length;i++){
		        if(groupCheckbox.eq(i).is(":checked")){//
		        	var name=groupCheckbox.eq(i).parent().siblings().find(".clickable").text();
	        	accountIds[i] =groupCheckbox[i].value;
	        	accountNames[i] =name;
		        }
		    }
		     //调用父类里的方法
		     parent.accountList(accountIds,accountNames); 
	         backOne();
	}
	
	function backOne(){
		var index=parent.layer.getFrameIndex(window.name);
		parent.layer.close(index);
	}
	</script>
	<script type="text/javascript">
	$("#checkbox").click(function () {
	    if($(this).is(':checked')){
	    	$("input[name='checkbox']").prop("checked",true);
	    }else{
	    	$("input[name='checkbox']").prop("checked",false);
	    }
	});

	</script>
</body>
</html>