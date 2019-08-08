layui.use(['form', 'table'], function() {
	var form = layui.form,
		table = layui.table;
	var status = [];

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
			var total;
			if(res.code == "0010") {
				code = 0;
				arr = res.data.content;
				total = res.data.numberOfElements;
			} else if(res.code == '0031') {
				code=0031
			}
			return {
				"code": code,
				"msg": res.msg,
				"count": total,
				"data": arr
			};
		},
		request: {
			pageName: 'pageNum',
			limitName: "pageSize"
		}
	});
	//条件查询
	form.on('submit(sreach)', function(data) {
		var param = data.field;
		if(param.statusId == -1) {
			param.statusId = '';
		}
		console.log(param);
		table.reload('idTest', {
			where: {
				"statusId": param.statusId,
				"staffName": param.staffName
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
						options = '<option value="" selected="selected">暂无状态信息</option>'
					} else {
						for(var i = 0; i < arr.length; i++) {
							options += '<option value="' + arr[i].id + '" >' + arr[i].name + '</option>'
						}
					}
					$('#status').append(options);
					$('#setStatus').append(options);
					form.render('select');
				} else {}
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
	});
	form.on('submit(updataDept)', function(data) {
		var param = data.field;
		if(param.statusId == -1) {
			setMsg('请选择状态', 2);
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
						options = '<option value="" selected="selected">暂无部门信息</option>'
					} else {
						for(var i = 0; i < arr.length; i++) {
							options += '<option value="' + arr[i].id + '" >' + arr[i].deptname + '</option>'
						}
					}
					$('#dept').append(options);
					form.render('select');
				} else {
					console.log(res.msg)
				}
			}
		});
	}
	form.on('submit(modifyDept)', function(data) {
		var param = data.field;
		if(param.deptId == -1) {
			setMsg('请选择需要调整的部门', 2);
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
						options = '<option value="" selected="selected">暂无角色信息</option>'
					} else {
						for(var i = 0; i < arr.length; i++) {
							options += '<option value="' + arr[i].id + '" >' + arr[i].rolename + '</option>'
						}
					}
					$('#role').append(options);
					form.render('select');
				} else {
					console.log(res.msg)
				}
			}
		});
	}
	form.on('submit(modifyRole)', function(data) {
		var param = data.field;
		if(param.deptId == -1) {
			setMsg('请选择需要调整的职位', 2);
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
	UrlSearch();

	function UrlSearch() { //获取url里面的参数
		var name, value;
		var str = location.href; //取得整个地址栏
		var num = str.indexOf("//")
		console.log(str)
		str = str.substr(num + 1); //取得所有参数   stringvar.substr(start [, length ]
		var arr = str.split("/"); //各个参数放到数组里
		console.log(arr)
		return arr[1];
	}
})