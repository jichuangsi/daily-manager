layui.use(['form', 'table'], function() {
	var form = layui.form,
		table = layui.table;
	var url = httpUrl() + '/ol/getolrecord1'
	form.on('select(setStatus)', function(data) {
		var param = data.value;
		if(param.statusId==-1){
			param.statusId='';
		}
		table.reload('idTest', {
			url: httpUrl() + '/ol/getolrecord1',
			header: {
				'accessToken': getToken()
			},
			where: {
				sttt:param.statusId,
				name: param.name
			}
		});
	});
	table.render({
		elem: '#demo',
		method: "post",
		async: false,
		url: httpUrl() + '/ol/getolrecord1',
		id:'idTest',
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
					title: '岗位',
					align: 'center'
				}, {
					field: 'd.overtimeleave.msg',
					title: '申请原因',
					align: 'center',
					templet: '<div>{{d.overtimeleave?d.overtimeleave.msg:""}}</div>'
				}, {
					field: 'd.overtimeleave.start',
					title: '申请时间',
					align: 'center',
					templet: function(d) {
						if(d.overtimeleave.start != 0) {
							return new Date(+new Date(d.overtimeleave.start) + 8 * 3600 * 1000).toISOString().replace(/T/g, ' ').replace(/\.[\d]{3}Z/, '')
						} else {
							return "-"
						}
					}
				}, {
					field: 'd.overtimeleave.time',
					title: '加班时间',
					align: 'center',
					templet: function(d) {
						if(d.overtimeleave.time != 0) {
							return new Date(+new Date(d.overtimeleave.time) + 8 * 3600 * 1000).toISOString().replace(/T/g, ' ').replace(/\.[\d]{3}Z/, '')
						} else {
							return "-"
						}
					}
				},
				{
					field: 'd.overtimeleave.stuas',
					title: '申请类型',
					align: 'center',
					templet: function(d) {
						if(d.overtimeleave.stuas == 1) {
							return "请假申请"
						} else if(d.overtimeleave.stuas == 2) {
							return "加班申请"
						}
					}
				}, {
					field: 'd.overtimeleave.stuas',
					title: '状态',
					align: 'center',
					templet: function(d) {
						if(d.overtimeleave.stuas2 == 0) {
							return "未审批"
						} else if(d.overtimeleave.stuas2 == 1) {
							return "已同意"
						} else if(d.overtimeleave.stuas2 == 2) {
							return "驳回"
						}
					}
				},

				{

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
		where: {
			sttt: '',
			name: ''
		},
		request: {
			pageName: 'pageNum',
			limitName: "pageSize"
		}
	});
	table.on('row(demo)', function(data) {
		var param = data.data;
	});
})