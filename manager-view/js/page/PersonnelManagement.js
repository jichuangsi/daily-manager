layui.use(['form', 'table'], function() {
	var form = layui.form,
		table = layui.table;
	var status = [];
	var user = JSON.parse(sessionStorage.getItem('userInfo'))
	var deptId;

	getRoleDep(user);
	table.render({
		elem: '#demo',
		method: "get",
		async: false,
		id: 'idTest',
		url: httpUrl() + '/backstaff/getStaffListByPage',
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
					field: 'name',
					title: '姓名',
					align: 'center'
				},
				{
					field: 'd.department.deptname',
					title: '部门',
					align: 'center',
					templet: '<div>{{d.department?d.department.deptname:""}}</div>'
				},
				{
					field: 'd.role.rolename',
					title: '岗位',
					align: 'center',
					templet: '<div>{{d.role?d.role.rolename:""}}</div>'
				}, {
					field: 'd.status.name',
					title: '状态',
					align: 'center',
					templet: '<div>{{d.status?d.status.name:""}}</div>'
				}, {
					field: 'schooldel',
					title: '调整部门/职位',
					align: 'center',
					toolbar: '#modify'
				}, {
					field: 'schooldel',
					title: '操作',
					align: 'center',
					toolbar: '#operation'
				}

			]
		],
		page: true,
		parseData: function(res) {
			var arr;
			var code;
			var totalElements;
			if(res.code == "0010") {
				code = 0;
				arr = res.data.content;
				totalElements = res.data.totalElements;
			} else if(res.code == '0031') {
				code = 0031
			}
			return {
				"code": code,
				"msg": res.msg,
				"count": totalElements,
				"data": arr
			};
		},
		request: {
			pageName: 'pageNum',
			limitName: "pageSize"
		},
		where: {
			deptId: deptId
		}
	});
	//条件查询
	form.on('submit(sreach)', function(data) {
		var param = data.field;
		if(param.statusId == -1 || param.statusId == undefined) {
			param.statusId = '';
		}
		table.reload('idTest', {
			where: {
				"statusId": param.statusId,
				"staffName": param.staffName,
				'deptId': param.roleDept
			}
		});
	});
	form.on('select(roleDept)', function(data) {
		var param = data.value;
		table.reload('idTest', {
			where: {
				'deptId': param
			}
		});
	});
	getStatus();
	//获取状态列表
	function getStatus() {
		$('#status').empty();
		$('#setStatus').empty();
		var options = '<option value="-1" selected="selected">' + "请选择状态" + '</option>';
		var arr = [];
		$.ajax({
			type: "get",
			url: httpUrl() + "/backstaff/getStatusList",
			async: false,
			headers: {
				'accessToken': getToken()
			},
			success: function(res) {
				if(res.code == '0010') {
					arr = res.data;
					if(arr.length == 0) {
						options = '<option value="-1" selected="selected">暂无状态信息</option>'
					} else {
						for(var i = 0; i < arr.length; i++) {
							options += '<option value="' + arr[i].id + '" >' + arr[i].name + '</option>'
						}
					}

				} else if(res.code == '0031') {
					layui.notice.info("提示信息：访问权限不足!");
				}
				$('#status').append(options);
				$('#setStatus').append(options);
				form.render('select');
			}
		});
	}
	//修改员工的状态
	table.on('row(demo)', function(data) {
		var param = data.data;
		form.val('test', {
			'wechat': param.wechat
		});
		form.val('testDept', {
			'wechat': param.wechat
		});
		form.val('testRole', {
			'wechat': param.wechat
		});
		// 删除员工
		// $(document).on('click', '#delPer', function() {
		// 	deleteStaffInfo(param.wechat,param.name);
		// });
		// 冻结
		$(document).on('click', '#frozen', function() {
			frozenStaffInfo(param.wechat,param.name);
		});
		//恢复
		$(document).on('click', '#relieve', function() {
			relieveStaffInfo(param.wechat,param.name);
		});
	});
	form.on('submit(updataDept)', function(data) {
		var param = data.field;
		if(param.statusId == -1 || param.statusId == undefined) {
			layui.notice.info("提示信息：请选择状态");
			return false;
		} else {
			$.ajax({
				type: "get",
				url: httpUrl() + "/backstaff/updateStaff?statusId=" + param.statusId + "&wechat=" + param.wechat,
				async: false,
				headers: {
					'accessToken': getToken()
				},
				success: function(res) {
					if(res.code == '0010') {
						table.reload('idTest');
						layui.notice.success("提示信息:修改成功!");
						layer.close(index);
					} else if(res.code == '0031') {
						layer.close(index);
						layui.notice.info("提示信息：权限不足");
					} else {
						console.log(res.msg)
						layer.close(index);
					}
				}
			});
			return false;
		}

	});
	/*获取修改部门*/
	getDept();

	function getDept() {
		$('#dept').empty();
		var options = '<option value="-1" selected="selected">' + "请选择部门" + '</option>';
		var arr = [];
		$.ajax({
			type: "get",
			url: httpUrl() + "/backdepartment/getDepartmentList",
			async: true,
			headers: {
				'accessToken': getToken()
			},
			success: function(res) {
				if(res.code == '0010') {
					arr = res.data;
					if(arr.length == 0) {
						options = '<option value="-1" selected="selected">暂无部门信息</option>'
					} else {
						for(var i = 0; i < arr.length; i++) {
							options += '<option value="' + arr[i].id + '" >' + arr[i].deptname + '</option>'
						}
					}

				} else if(res.code == '0031') {
					layui.notice.info("提示信息：访问权限不足!");
				}
				$('#dept').append(options);
				form.render('select');
			}
		});
	}
	form.on('submit(modifyDept)', function(data) {
		var param = data.field;
		if(param.deptId == -1 || param.deptId == undefined) {
			layui.notice.info("提示信息：请选择需要调整的部门");
			return false;
		} else {
			$.ajax({
				type: "get",
				url: httpUrl() + "/backstaff/updateStaffDept?deptId=" + param.deptId + "&wechat=" + param.wechat,
				async: false,
				headers: {
					'accessToken': getToken()
				},
				success: function(res) {
					if(res.code == '0010') {
						table.reload('idTest');
						layui.notice.success("提示信息:修改成功!");
						layer.close(index);
					} else {
						console.log(res.msg)
						layer.close(index);
					}
				}
			});
			return false;
		}

	});
	/*获取修改角色*/
	getRole();

	function getRole() {
		$('#role').empty();
		var options = '<option value="-1" selected="selected">' + "请选择职位" + '</option>';
		var arr = [];
		$.ajax({
			type: "get",
			url: httpUrl() + "/backrole/getRoleList",
			async: true,
			headers: {
				'accessToken': getToken()
			},
			success: function(res) {
				if(res.code == '0010') {
					arr = res.data;
					if(arr.length == 0) {
						options = '<option value="-1" selected="selected">暂无角色信息</option>'
					} else {
						for(var i = 0; i < arr.length; i++) {
							options += '<option value="' + arr[i].id + '" >' + arr[i].rolename + '</option>'
						}
					}

				} else if(res.code == "0031") {
					layui.notice.info("提示信息：访问权限不足!");
				}
				$('#role').append(options);
				form.render('select');
			}
		});
	}
	form.on('submit(modifyRole)', function(data) {
		var param = data.field;
		if(param.roleId == -1 || param.roleId == undefined) {
			layui.notice.info("提示信息：请选择需要调整的职位");
			return false;
		} else {
			$.ajax({
				type: "get",
				url: httpUrl() + "/backstaff/updateStaffRole?roleId=" + param.roleId + "&wechat=" + param.wechat,
				async: false,
				headers: {
					'accessToken': getToken()
				},
				success: function(res) {
					if(res.code == '0010') {
						table.reload('idTest');
						layer.close(index);
						layui.notice.success("提示信息:修改成功!");

					} else if(res.code == '0031') {
						layer.close(index);
						layui.notice.info("提示信息：权限不足");
					} else {
						console.log(res.msg)
						layer.close(index);
					}
				}
			});
			return false;
		}

	});
	//根据角色获取角色所管理的部门
	function getRoleDep(user) {
		var id = user.id;
		var options = '';
		var arr = [];
		$('#roleDept').empty();
		$.ajax({
			type: "get",
			async: false,
			url: httpUrl() + '/backrole/getRoleDepartmentByRoleId?roleId=' + id,
			headers: {
				'accessToken': getToken()
			},
			success: function(res) {
				if(res.code == '0010') {
					arr = res.data;
					if(arr.length == 0) {
						$('#allDept').empty();
					} else {
						for(var i = 0; i < arr.length; i++) {
							options += '<option value="' + arr[i].deptId + '" >' + arr[i].deptName + '</option>'
						}
						$('#roleDept').append(options);
						$("#roleDept option[value=" + arr[0].deptId + "]").prop("selected", true);
						form.render('select');
						deptId = arr[0].deptId
					}
				} else {
					console.log(res.msg)
				}
			}
		});
	}
	//删除人员
	function deleteStaffInfo(openId,name){
		layer.confirm('确认要删除'+name+'吗？', function(index) {
			$.ajax({
				type: "post",
				url: httpUrl() + "/backstaff/deleteStaffInfo/"+openId,
				async: false,
				headers: {
					'accessToken': getToken()
				},
				success: function(res) {
					if(res.code == '0010') {
						layer.close(index);
						layui.notice.success("提示信息:删除成功!");
						table.reload('idTest');
					} else if(res.code == '0031') {
						layer.close(index);
						layui.notice.info("提示信息：权限不足");
					} else {
						table.reload('idTest');
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
	
	function frozenStaffInfo(openId,name){
		layer.confirm('确认要冻结'+name+'吗？', function(index) {
			$.ajax({
				type: "post",
				url: httpUrl() + "/backstaff/frozenStaffInfo/"+openId,
				async: false,
				headers: {
					'accessToken': getToken()
				},
				success: function(res) {
					if(res.code == '0010') {
						layer.close(index);
						layui.notice.success("提示信息:冻结成功!");
						table.reload('idTest');
					} else if(res.code == '0031') {
						layer.close(index);
						layui.notice.info("提示信息：权限不足");
					} else {
						table.reload('idTest');
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
	function relieveStaffInfo(openId,name){
		layer.confirm('确认要恢复'+name+'吗？', function(index) {
			$.ajax({
				type: "post",
				url: httpUrl() + "/backstaff/thawStaffInfo/"+openId,
				async: false,
				headers: {
					'accessToken': getToken()
				},
				success: function(res) {
					if(res.code == '0010') {
						layer.close(index);
						layui.notice.success("提示信息:恢复成功!");
						table.reload('idTest');
					} else if(res.code == '0031') {
						layer.close(index);
						layui.notice.info("提示信息：权限不足");
					} else {
						table.reload('idTest');
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

})