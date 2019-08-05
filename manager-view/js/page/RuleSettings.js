layui.use(['form', 'table', 'laydate'], function() {
	var laydate = layui.laydate,
		form = layui.form,
		table = layui.table;
	$('#map').on('click', function() {
		getMap()
	})

	function getMap() {
		index = layer.open({
			type: 2,
			area: ['90%', ' 90% '],
			anim: 2,
			title: '获取坐标(点击获取经纬度)',
			maxmin: true,
			shadeClose: true,
			content: 'Map.html',
			end: function() {
				getmap()
			}
		});
	}

	function getmap() {
		var map = JSON.parse(sessionStorage.getItem('map'))
		if(map != null) {
			form.val('test', {
				'lng': map.lng,
				'lat': map.lat
			})
		}
	}
	//提交
	/*	form.on('submit()',function(data){
			var param =data.field;
			$.ajax({
					type: "post",
					url: httpUrl() + "/class/deleteClass/",
					async: false,
					headers: {
						'accessToken': getToken()
					},
					success: function(res) {
						if(res.code == '0010') {
							layer.close(index);
						} else {

						}
					},
					error: function(res) {
						console.log(res.msg)
					}
				})
		});*/
})