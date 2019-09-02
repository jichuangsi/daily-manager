layui.use(['form', 'table'], function() {
	var form = layui.form,
		table = layui.table;
		/*当天*/
	table.render({
		elem: '#demo',
		method: "get",
		async: false,
		url: httpUrl() + '/backdepartment/getDepartmentList',
		headers: {
			'accessToken': getToken()
		},
		cols: [
			[{
					field: 'id',
					title: '序号',
					type: 'numbers'
				},
				{
					field: 'deptname',
					title: '部门名称',
					align: 'center'
				},
				{
					field: 'deptdescribe',
					title: '部门信息',
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
		page: false,
		parseData: function(res) {
			var arr = [];
			var code, total;
			if(res.code == "0010") {
				code = 0;
				arr = res.data;
				total = arr.length;
			} else if(res.code == '0031') {
				code = 0031
			}
			return {
				"code": code,
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
			url: httpUrl() + "/backdepartment/saveDepartment",
			async: false,
			headers: {
				'accessToken': getToken()
			},
			contentType: 'application/json',
			data: JSON.stringify(param),
			success: function(res) {
				if(res.code == '0010') {
					table.reload('demo');
					layui.notice.success("提示信息:成功!");
				} else if(res.code == '0031') {
					layui.notice.info("提示信息：权限不足");
				} else if(res.code=='0050') {
					layui.notice.error("提示信息:"+res.msg);
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
			'deptname': param.deptname,
			'deptdescribe': param.deptdescribe
		});
	});
	//删除部门
	function DelDepartment(deptId) {
		layer.confirm('确认要删除吗？', function(index) {
			$.ajax({
				type: "get",
				url: httpUrl() + "/backdepartment/deleteDepartment/" + deptId,
				async: false,
				headers: {
					'accessToken': getToken()
				},
				success: function(res) {
					if(res.code == '0010') {
						table.reload('demo');
						layer.close(index);
						layui.notice.success("提示信息:删除成功!");
					} else if(res.code == '0031') {
						layer.close(index);
						layui.notice.info("提示信息：权限不足");
					} else {
						table.reload('demo');
						layer.close(index);
						layui.notice.error("提示信息:" + res.msg);
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
			url: httpUrl() + "/backdepartment/updateDepartment",
			async: false,
			headers: {
				'accessToken': getToken()
			},
			contentType: 'application/json',
			data: JSON.stringify(param),
			success: function(res) {
				if(res.code == '0010') {
					table.reload('demo');
					layer.close(index);
					layui.notice.success("提示信息:成功!");
				} else if(res.code == '0031') {
					layer.close(index);
					layui.notice.info("提示信息：权限不足");
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
	/*角色列表*/
	table.render({
		elem: '#role',
		method: "get",
		async: false,
		url: httpUrl() + '/backrole/getRoleList',
		headers: {
			'accessToken': getToken()
		},
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
			} else if(res.code == '0031') {
				code = 0031
			}
			return {
				"code": code,
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
			headers: {
				'accessToken': getToken()
			},
			contentType: 'application/json',
			data: JSON.stringify(param),
			success: function(res) {
				if(res.code == '0010') {
					table.reload('role');
					layui.notice.success("提示信息:成功!");
				} else if(res.code == '0031') {
					layer.close(index);
					layui.notice.info("提示信息：权限不足");
				} else {
					table.reload('role');
					layui.notice.error("提示信息:失败啦!");
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
				headers: {
					'accessToken': getToken()
				},
				success: function(res) {
					if(res.code == '0010') {
						table.reload('role');
						layer.close(index);
						layui.notice.success("提示信息:删除成功!");
					} else if(res.code == '0031') {
						layer.close(index);
						layui.notice.info("提示信息：权限不足");
					} else {
						table.reload('role');
						layer.close(index);
						layui.notice.error("提示信息:" + res.msg);
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
			headers: {
				'accessToken': getToken()
			},
			contentType: 'application/json',
			data: JSON.stringify(param),
			success: function(res) {
				if(res.code == '0010') {
					table.reload('role');
					layer.close(index);
					layui.notice.success("提示信息:成功!");
				} else if(res.code == '0031') {
					layer.close(index);
					layui.notice.info("提示信息：权限不足");
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