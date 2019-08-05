layui.use(['form', 'table'], function() {
	var form = layui.form,
		table = layui.table;
	table.render({
		elem: '#demo',
		method: "get",
		async: false,
		url: httpUrl() + '/backrole/getRoleUrlList',
		cols: [
			[{
					field: 'id',
					title: '序号',
					type: 'numbers'
				},
				{
					field: 'name',
					title: 'url名称',
					align: 'center'
				},
				{
					field: 'url',
					title: 'url',
					align: 'center'
				},

				{
					field: 'deptdescribe',
					title: '修改',
					align: 'center',
					toolbar: '#operation'
				}, {
					field: 'deptdescribe',
					title: '删除',
					align: 'center',
					toolbar: '#del'
				}
			]
		],
		parseData: function(res) {
			var arr = [];
			var code, total;
			if(res.code == "0010") {
				code = 0;
				arr = res.data;
				total = arr.length;
			}
			return {
				"code": 0,
				"msg": res.msg,
				"count": total,
				"data": arr
			};
		}
	});
	form.on('submit(addDept)', function(data) {
		var param = data.field;
		$.ajax({
			type: "post",
			url: httpUrl() + "/backrole/saveRoleUrl",
			async: false,
			contentType: 'application/json',
			data: JSON.stringify(param),
			success: function(res) {
				if(res.code == '0010') {
					table.reload('demo');
					layui.notice.success("提示信息:成功!");
				} else {
					layui.notice.error("提示信息:失败啦!");
				}
			},
			error: function(res) {
				console.log(res.msg)
			}
		})
		return false;
	});
	table.on('row(demo)', function(data) {
		var param = data.data;
		$(document).on('click', '#delDept', function() {
			DelDepartment(param.id);
		});
		form.val('test', {
			'id': param.id,
			'url': param.url,
			'name': param.name
		});
	});
	//删除部门
	function DelDepartment(id) {
		layer.confirm('确认要删除吗？', function(index) {
			console.log(id)
			$.ajax({
				type: "get",
				url: httpUrl() + "/backrole/deleteRoleUrl/" + id,
				async: false,
				success: function(res) {
					if(res.code == '0010') {
						table.reload('demo');
						layer.close(index);
						layui.notice.success("提示信息:删除成功!");
					} else {
						layui.notice.error("提示信息:删除失败!");
					}
				},
				error: function(res) {
					console.log(res.msg)
				}
			})

		});
	}
	//修改
	form.on('submit(updataDept)', function(data) {
		var param = data.field;
		$.ajax({
			type: "post",
			url: httpUrl() + "/backrole/updateRoleUrl",
			async: false,
			contentType: 'application/json',
			data: JSON.stringify(param),
			success: function(res) {
				if(res.code == '0010') {
					table.reload('demo');
					layer.close(index);
					layui.notice.success("提示信息:成功!");
				} else {
					layer.close(index);
					layui.notice.error("提示信息:修改失败!");
				}
			},
			error: function(res) {
				console.log(res.msg)
			}
		})
		return false;
	});
	getRole();

	function getRole() {
		$('#status').empty();
		var options = '<option value="-1" selected="selected">' + "请选择角色用户" + '</option>';
		var arr;
		$.ajax({
			type: "get",
			url: httpUrl() + "/backrole/getUsemoduleList",
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
							options += '<option value="' + arr[i].id + '" >' + arr[i].name + '</option>'
						}
					}
					$('#status').append(options);
					form.render('select');
				} else {
					console.log(res.msg);
				}
			}
		});
	}
	/*角色列表*/
	table.render({
		elem: '#role',
		method: "get",
		async: false,
		url: httpUrl() + '/backrole/getRoleList',
		cols: [
			[{
					field: 'id',
					title: '序号',
					type: 'numbers'
				},
				{
					field: 'rolename',
					title: '角色名称',
					align: 'center'
				},
				{
					field: 'deptdescribe',
					title: '修改',
					align: 'center',
					toolbar: '#operationRole'
				}, {
					field: 'deptdescribe',
					title: '删除',
					align: 'center',
					toolbar: '#delRole'
				}
			]
		],
		page: false,
		parseData: function(res) {
			var arr = [];
			var code, total;
			if(res.code == "0010") {
				code = 0;
				arr = res.data;
				total = arr.length;
			}
			return {
				"code": 0,
				"msg": res.msg,
				"count": total,
				"data": arr
			};
		}
	});
	form.on('submit(addRole)', function(data) {
		var param = data.field;
		$.ajax({
			type: "post",
			url: httpUrl() + "/backrole/saveRole",
			async: false,
			contentType: 'application/json',
			data: JSON.stringify(param),
			success: function(res) {
				if(res.code == '0010') {
					table.reload('role');
					layui.notice.success("提示信息:成功!");
				} else {
					layui.notice.error("提示信息:删除失败啦!");
				}
			},
			error: function(res) {
				console.log(res.msg)
			}
		})
		return false;
	});
	table.on('row(role)', function(data) {
		var param = data.data;
		$(document).on('click', '#Role', function() {
			DelRole(param.id);
		});
		form.val('test2', {
			'id': param.id,
			'rolename': param.rolename
		});
	});
	//删除角色
	function DelRole(Id) {
		layer.confirm('确认要删除吗？', function(index) {
			$.ajax({
				type: "get",
				url: httpUrl() + "/backrole/deleteRole/" + Id,
				async: false,
				success: function(res) {
					if(res.code == '0010') {
						table.reload('role');
						layer.close(index);
						layui.notice.success("提示信息:删除成功!");
					} else {
						layui.notice.error("提示信息:删除失败!");
					}
				},
				error: function(res) {
					console.log(res.msg)
				}
			})

		});
	}
	//修改
	form.on('submit(updataRole)', function(data) {
		var param = data.field;
		$.ajax({
			type: "post",
			url: httpUrl() + "/backrole/updateRole",
			async: false,
			contentType: 'application/json',
			data: JSON.stringify(param),
			success: function(res) {
				if(res.code == '0010') {
					table.reload('role');
					layer.close(index);
					layui.notice.success("提示信息:成功!");
				} else {
					layer.close(index);
					layui.notice.error("提示信息:修改失败!");
				}
			},
			error: function(res) {
				console.log(res.msg)
			}
		})
		return false;
	});
})