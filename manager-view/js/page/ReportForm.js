layui.use(['form', 'table', 'laydate'], function() {
	var form = layui.form,
		table = layui.table,
		laydate = layui.laydate;
	var date = new Date();
	date.setMonth(date.getMonth() - 1);
	var dateStart = date.getFullYear() + "-" + (date.getMonth() + 1) + "-" + date.getDate();
	var dateEnd = date.getFullYear() + "-" + (date.getMonth() + 2) + "-" + date.getDate();
	//获取角色登陆的信息
	var user = JSON.parse(sessionStorage.getItem('userInfo'))
	var deptId;
	getRoleDep(user);
	table.render({
		elem: '#demo',
		method: "post",
		async: false,
		url: httpUrl() + '/kq/getBB',
		id: 'idTest',
		headers: {
			'content-type': 'application/x-www-form-urlencoded',
			'accessToken': getToken()
		},
		cols: [
			[{
					field: 'id',
					title: '序号',
					type: 'numbers'
				},
				{
					field: 'peopleName',
					title: '姓名',
					align: 'center'
				},
				{
					field: 'department',
					title: '部门',
					align: 'center'
				},
				{
					field: 'jurisdiction',
					title: '职位',
					align: 'center'
				},

				{
					field: 'kq',
					title: '考勤正常次数',
					align: 'center',
				}, {
					field: 'qq',
					title: '缺勤次数',
					align: 'center'
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
				arr = res.data;
				total = arr.length;
			}
			return {
				"code": code,
				"msg": res.msg,
				"count": res.pageSize,
				"data": arr
			};
		},
		where: {
			deptId: user.deptId,
			timeStart: dateStart,
			timeEnd: dateEnd,
			name: ''
		},
		request: {
			pageName: 'pageNum',
			limitName: "pageSize"
		}
	});
	laydate.render({
		elem: '#test',
		range: '~'
	});

	form.on('select(roleDept)', function(data) {
		var param = data.value;
		table.reload('idTest', {
			where: {
				'deptId': param
			}
		});
	});	
	form.on('submit(sreach)', function(data) {
		var param = data.field;
		var date = param.date.split('~');
		if(param.date == '') {
			var date = new Date();
			date.setMonth(date.getMonth() - 1);
			var dateStart = date.getFullYear() + "-" + (date.getMonth() + 1) + "-" + date.getDate();
			var dateEnd = date.getFullYear() + "-" + (date.getMonth() + 2) + "-" + date.getDate();
		} else {
			var dateStart = date[0];
			var dateEnd = date[1];
		}
		//默认获取一个月前的
		table.reload('idTest', {
			url: httpUrl() + '/kq/getBB',
			header: {
				'content-type': 'application/x-www-form-urlencoded',
				'accessToken': getToken()
			},
			where: {
				deptId:param.roleDept,
				timeStart: dateStart,
				timeEnd: dateEnd,
				name: param.name
			}
		});
	});
	//监听部门的管理列表
	var deptId;
	form.on('select(roleDept)', function(data) {
		var param = data.value;
		table.reload('idTest', {
			where: {
				'deptId': param
			}
		});
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
						$("#roleDept option[value=" + user.deptId + "]").prop("selected", true);
						form.render('select');
						deptId = arr[0].deptId
					}
				} else {
					console.log(res.msg)
				}
			}
		});
	}
})