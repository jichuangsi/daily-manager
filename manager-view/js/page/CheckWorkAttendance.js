layui.use(['form', 'table'], function() {
	var form = layui.form,
		table = layui.table;
	//获取登陆者的信息
	function getUser() {
		var user =JSON.parse(sessionStorage.getItem('userInfo'))
		if(user.roleName=="M"){
			user.wechat='';	
		}
		return user;
	}
	getUser();
	table.render({
		elem: '#demo',
		method: "post",
		id: 'idTest',
		async: false,
		url: httpUrl() + '/kq/getTDBB',
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
					field: 'stuas',
					title: '考勤',
					align: 'center',
					templet: function(d) {
						if(d.stuas == 1) {
							return "上班打卡"
						} else if(d.stuas == 2) {
							return "下班打卡"
						}
					}
				}, {
					field: 'time',
					title: '打卡时间',
					align: 'center',
					templet: function(d) {
						if(d.time != 0) {
							return new Date(+new Date(d.time) + 8 * 3600 * 1000).toISOString().replace(/T/g, ' ').replace(/\.[\d]{3}Z/, '')
						} else {
							return "-"
						}
					}
				},
				{
					field: 'stuas2',
					title: '状态',
					align: 'center',
					toolbar: '#colorStatus'
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
				"code": 0,
				"msg": res.msg,
				"count": total,
				"data": arr
			};
		},
		request: {
			pageName: 'pageNum',
			limitName: "pageSize"
		},where:{
			dpid:getUser().deptId,
			jpid:getUser().roleId,
			openid:getUser().wechat
		}
	});

	form.on('submit(sreach)', function(data) {
		var param = data.field;
		table.reload('idTest', {
			url: httpUrl() + '/kq/getTDBB',
			header: {
				'accessToken': getToken()
			},
			where: {
				name: param.name
			}
		});
	})
})