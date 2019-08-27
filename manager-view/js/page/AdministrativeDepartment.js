layui.use(['form', 'table', 'transfer'], function() {
	var form = layui.form,
		table = layui.table,
		transfer = layui.transfer;
	var dep = [],
		roleDep = [];
	getinit()
	//获取部门
	function getinit() {
		$.ajax({
			type: "get",
			async: false,
			url: httpUrl() + '/backdepartment/getDepartmentList',
			headers: {
				'accessToken': getToken()
			},
			success: function(res) {
				if(res.code == '0010') {
					dep = res.data;
					return dep;
				}
			}
		});
	}
	var tableId;
	form.on('select(role)', function(data) {
		if(data.value != '-1') {
			tableId = data.value;
			getRoleDep(tableId)
			var list = [];
			for(var i = 0; i < roleDep.length; i++) {
				list.push(
					roleDep[i].value
				)
			}
			transfer.reload('demo1', {
				title: ['全部部门', '管理部门'],
				data: dep,
				parseData: function(res) {
					return {
						"value": res.id,
						"title": res.deptname
					}
				},
				value: list
			});
		}
	});
	transfer.render({
		elem: '#demo',
		title: ['全部部门', '管理部门'],
		data: dep,
		id: 'demo1',
		parseData: function(res) {
			return {
				"value": res.id,
				"title": res.deptname
			}
		},
		value: roleDep,
		onchange: function(data, index) {
			if(tableId == undefined) {
				layui.notice.error('请选择用户')
			} else {
				if(index === 0) {
					var list = [];
					for(var i = 0; i < data.length; i++) {
						list.push({
							roleId: tableId,
							deptId: data[i].value
						})
					}
					var array = {
						'roleDepartmentList': list
					}
					addDept(array);
				} else {
					var idList = [];
					for(var i = 0; i < data.length; i++) {
						for(var j = 0; j < roleDep.length; j++) {
							if(data[i].value == roleDep[j].value) {
								idList.push({
									id: roleDep[j].id
								});
							}
						}
					}
					var array = {
						'roleDepartmentList': idList
					}
					delDept(array);
				}
			}

		}
	});
	var getData = transfer.getData('demo1');

	getRole();
	/*获取角色列表*/
	function getRole() {
		$('#status').empty();
		var options = '<option value="-1" selected="selected">' + "请选择角色用户" + '</option>';
		var arr;
		$.ajax({
			type: "get",
			url: httpUrl() + "/backuser/findBackUserByRoleId",
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
							options += '<option value="' + arr[i].id + '" >' + arr[i].userName + '</option>'
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
	//获取角色所管理的部门
	function getRoleDep(id) {
		$.ajax({
			type: "get",
			async: false,
			url: httpUrl() + '/backrole/getRoleDepartmentByRoleId?roleId=' + id,
			headers: {
				'accessToken': getToken()
			},
			success: function(res) {
				if(res.code == '0010') {
					roleDep = res.data;
					updateName(roleDep);
					return roleDep;
				}
			}
		});
		return false;
	}

	function updateName(array) {
		var keyMap = {
			deptId: 'value',
			deptName: 'title'
		}
		for(var i = 0; i < array.length; i++) {
			var obj = array[i];
			for(var key in obj) {
				var newKey = keyMap[key];
				if(newKey) {
					obj[newKey] = obj[key];
					delete obj[key];
				}
			}
		}
	}
	//向角色添加管理部门
	function addDept(array) {
		$.ajax({
			type: "post",
			url: httpUrl() + "/backrole/batchAddRoleDepartment",
			async: false,
			headers: {
				'accessToken': getToken()
			},
			contentType: 'application/json',
			data: JSON.stringify(array),
			success: function(res) {
				if(res.code == '0010') {
					layui.notice.success("提示信息:成功!");
				} else {
					layui.notice.error(res.msg)
				}
			}
		});
	}
	//取消角色管理的部门
	function delDept(array) {
		$.ajax({
			type: "post",
			url: httpUrl() + "/backrole/batchDeleteRoleDepartment ",
			async: false,
			headers: {
				'accessToken': getToken()
			},
			contentType: 'application/json',
			data: JSON.stringify(array),
			success: function(res) {
				if(res.code == '0010') {
					layui.notice.success("提示信息:成功!");
				} else {
					layui.notice.error(res.msg)
				}
			}
		});
	}
})