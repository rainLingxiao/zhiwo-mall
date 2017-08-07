<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/include/taglib.jsp"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>用户列表</title>
<%@ include file="/WEB-INF/include/easyui-css.jsp"%>
<%@ include file="/WEB-INF/include/easyui-js.jsp"%>
<style type="text/css">
#fm {
	margin: 0;
	padding: 10px 30px;
}

.ftitle {
	font-size: 14px;
	font-weight: bold;
	color: #666;
	padding: 5px 0;
	margin-bottom: 10px;
	border-bottom: 1px solid #ccc;
}

.fitem {
	margin-bottom: 5px;
}

.fitem label {
	display: inline-block;
	width: 80px;
}
</style>
</head>
<body>
	<div id="toolbar">
		<a href="#" class="easyui-linkbutton" iconCls="icon-add" plain="true"
			onclick="create('user')">新增</a> <a href="#" class="easyui-linkbutton"
			iconCls="icon-edit" plain="true" onclick="editCategory()">编辑</a> <a href="#"
			class="easyui-linkbutton" iconCls="icon-remove" plain="true"
			onclick="destroy()">删除</a>
	</div>
	<table id="dg" 
		title="用户列表" 
		class="easyui-datagrid"
		url="${ctx}/user/select" 
		toolbar="#toolbar" 
		rownumbers="true"
		fitColumns="true" 
		fit="true" 
		singleSelect="true">
		<thead>
			<tr>
				<th data-options="field:'ck',checkbox:true"></th>
				<th data-options="field:'id',align:'center'">id</th>
				<th data-options="field:'username',align:'center'">账户名称</th>
				<th data-options="field:'createDate',align:'center',width:100">创建日期</th>
				<th data-options="field:'updateDate',align:'center',width:100">更新日期</th>
				<!-- <th data-options="field:'By',align:'center',width:100">创建人</th>
				<th data-options="field:'updateBy',align:'center',width:100">更新人</th> -->
				<th data-options="field:'opt',align:'center',formatter:formatOpt">操作</th>
			</tr>
		</thead>
	</table>
	<script>
		
		//操作
		function formatOpt(value, rec) {
			var btn = '<div style="padding: 5px;">';
//			<%
//				if(SecurityUtils.getSubject()!=null&&SecurityUtils.getSubject().isPermitted("system:user:delete")){
//				%>
				btn += '<a href="#" class="btn btn-danger btn-xs" class="btn btn-danger btn-xs"  onclick="deleteById(\'tgrid\',\''
					+ rec.id + '\',\'user\')"><i class="icon-trash"></i>&nbsp;&nbsp;删除 </a>';
					btn += "&nbsp;&nbsp;"
//				<%
//				}
//			%>
			<!-- <%
				if(SecurityUtils.getSubject()!=null&&SecurityUtils.getSubject().isPermitted("system:user:edit")){
				%> -->
				btn += '<a href="#" class="btn btn-success btn-xs"  onclick="update(\''
					+ rec.id + '\',\'user\')"><i class="icon-edit"></i>&nbsp;&nbsp;编辑 </a>';
				<!-- <%
				}
							%> -->
			
			btn += '</div>';
			return btn;
		}

		// 删除
		function destroy() {
			var row = $('#dg').datagrid('getSelected');
			if (row) {
				$.messager.confirm('确定', '确定删除？', function(r) {
					if (r) {
						$.post('${ctx}/user/delete', {
							id : row.id
						}, function(result) {
							if (result > 0) {
								$('#dg').datagrid('reload'); // reload the user data
							} else {
								$.messager.show({ // show error message
									title : 'Error',
									msg : result
								});
							}
						}, 'json');
					}
				});
			}
		}
		
	</script>
	
	<%@ include file="/WEB-INF/include/easyui-footerjs.jsp"%>
</body>
</html>