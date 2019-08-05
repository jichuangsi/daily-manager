layui.use(['form', 'table'], function() {
	var form = layui.form,
		table = layui.table;
	table.render({
		elem: '#demo',
		method: "get",
		async: false,
		url: '../json/data.json',
		cols: [
			[{
					field: 'id',
					title: '序号',
					type: 'numbers'
				},
				{
					field: 'cz',
					title: '行为名称',
					align: 'center'
				},
				{
					field: 'name',
					title: '执行者',
					align: 'center'
				},
				{
					field: 'end',
					title: '执行时间',
					align: 'center'
				}, {
					field: 'schooldel',
					title: '操作',
					align: 'center',
					toolbar: '#operation'
				}
			]
		],
		page: false,
		parseData: function(res) {
			var arr, code, total;
			if(res.code == "0010") {
				code = 0;
				arr = res.data.list;
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
	//删除日志
	function DelLog(id) {
		layer.confirm('确认要删除吗？', function(index) {
			$.ajax({
				type: "get",
				url: httpUrl() + "/class/deleteClass/",
				async: false,
				headers: {
					'accessToken': getToken()
				},
				success: function(res) {
					if(res.code == '0010') {
						table.reload('demo');
						layer.close(index);
					} else {

					}
				},
				error: function(res) {
					console.log(res.msg)
				}
			})

		});
	}
})