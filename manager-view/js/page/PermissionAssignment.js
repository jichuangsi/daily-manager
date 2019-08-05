layui.use(['form', 'table', 'tree', 'util'], function() {
	var form = layui.form,
		table = layui.table,
		tree = layui.tree,
		util = layui.util;
	getRole();
	var ModularList=[];
	/*获取角色列表*/
	function getRole() {
		$('#status').empty();
		var options = '<option value="-1" selected="selected">' + "请选择角色用户" + '</option>';
		var arr;
		$.ajax({
			type: "get",
			url: httpUrl() + "/backrole/getRoleList",
			async: false,
			headers: {
				'accessToken': getToken()
			},
			success: function(res) {
				if(res.code == '0010') {
					var arr = [];
					arr = res.data;
					if(arr == null || arr == undefined || arr.length == 0) {
						options = '<option value="" selected="selected">暂无角色信息请先去添加角色</option>'
					} else {
						for(var i = 0; i < arr.length; i++) {
							options += '<option value="' + arr[i].id + '" >' + arr[i].rolename + '</option>'
						}
					}
					$('#status').append(options);
					form.render('select');
				} else {
					setMsg(res.msg, 2);
				}
			}
		});
	}
	/*获取模块分类*/
	function getSort() {
		$.ajax({
			type: "get",
			url: httpUrl() + "/backrole/getUsemoduleList",
			async: false,
			headers: {
				'accessToken': getToken()
			},
			contentType: 'application/json',
			success: function(res) {
				if(res.code == '0010') {
					ModularList = res.data;
				}
			}
		});
	}
	/**/
	
})