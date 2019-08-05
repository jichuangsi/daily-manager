layui.use(['form', 'table', 'tree', 'util'], function() {
	var form = layui.form,
		table = layui.table,
		tree = layui.tree,
		util = layui.util;
	var sortList = [],
		roleUrl = [],
		urlList = [];
	getSort();
	getURL();
	getRole();
	setTree();
	var URLTree, RoleTree;

	//修改name为title
	function updateName(array) {
		var keyMap = {
			name: 'title'
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
	//获取模块
	function getSort() {
		$.ajax({
			type: "get",
			url: httpUrl() + "/backrole/getStaticPageList",
			async: false,
			headers: {
				'accessToken': getToken()
			},
			contentType: 'application/json',
			success: function(res) {
				if(res.code == '0010') {
					sortList = res.data;
				}
			}
		});
	}
	//获取url列表
	function getURL() {
		$.ajax({
			type: "get",
			url: httpUrl() + "/backrole/getRoleUrlList",
			async: false,
			headers: {
				'accessToken': getToken()
			},
			success: function(res) {
				if(res.code == '0010') {
					urlList = res.data;
				}
			}
		});
	}
	//获取角色URl列表
	function getRoleUrl(id) {
		$.ajax({
			type: "get",
			url: httpUrl() + "/backrole/getAllRoleUrlByRoleId/" + id,
			async: false,
			headers: {
				'accessToken': getToken()
			},
			contentType: 'application/json',
			success: function(res) {
				if(res.code == '0010') {
					roleUrl = res.data;
				}
			}
		});
	}

	//拼接数组构建tree
	function setTree(roleUrl) {
		var children = [];
		var nodes = [];
		if(urlList.length === 0) {
			getURL();
		}
		var list = [],
			listUrl = [];
		list = urlList;
		updateName(sortList);
		updateName(list);
		if(roleUrl != null && roleUrl != undefined) {
			for(var j = 0; j < roleUrl.length; j++) {
				for(var i = 0; i < list.length; i++) {
					if(roleUrl[j].id == list[i].id) {
						list.splice(i, 1)
					}
				}
			}
		}
		for(var i = 0; i < sortList.length; i++) {
			for(var j = 0; j < list.length; j++) {
				if(sortList[i].id == list[j].usewayid) {
					children.push(list[j]);
				}
			}
			sortList[i].children = children;
			children = [];
		}
		URLTree = tree.render({
			elem: '#urlTree',
			showCheckbox: true,
			data: sortList,
			accordion: true,
			showLine: false,
			id: 'treeUrlId'
		});
	}
	//角色的url列表
	function roleTree() {
		getSort();
		var children = [];
		updateName(roleUrl);
		updateName(sortList);
		for(var i = 0; i < sortList.length; i++) {
			for(var j = 0; j < roleUrl.length; j++) {
				if(roleUrl[j].staticPageId == sortList[i].id) {
					children.push(roleUrl[j]);
				}
			}
			sortList[i].children = children;
			children = [];
		}
		RoleTree = tree.render({
			elem: '#roleTree',
			showCheckbox: true,
			data: sortList,
			accordion: true,
			showLine: false,
			id: 'roleUrlTree'
		});
		tree.reload('roleUrlTree', {});
		if(roleUrl != null) {
			setTree(roleUrl);
		}
	}
	getRole();
	/*获取角色列表*/
	function getRole() {
		$('#status').empty();
		var options = '<option value="-1" selected="selected">' + "请选择角色用户" + '</option>';
		var arr;
		$.ajax({
			type: "get",
			url: httpUrl() + "/backrole/getRoleList",
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
							options += '<option value="' + arr[i].id + '" >' + arr[i].rolename + '</option>'
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
	var tableId;
	form.on('select(role)', function(data) {
		if(data.value != '-1') {
			tableId = data.value;
			getRoleUrl(tableId);
			roleTree();
		}
		$(document).on('click', '#del', function() {
			getRolebox(tableId)
		});
		$(document).on('click', '#add', function() {
			getUrlbox(tableId)
		});

	});
	//添加到角色里面去
	function getUrlbox(tableId) {
		var checkedData = tree.getChecked('treeUrlId'); //获取选中节点的数据

		if(checkedData.length == 0) {
			layui.notice.warning("提示信息:请选择相关权限");
		} else {
			var children=[];
			for(var i=0;i<checkedData.length;i++){
				children.push(checkedData[i].children[0])
			}
			var list = [];
			console.log(tableId + "+" + children)
			for(var i = 0; i < children.length; i++) {
				list.push({
					rid: tableId,
					uid: children[i].id
				})
			}
			var urlRelations = {
				'urlRelations': list
			}
			$.ajax({
				type: "post",
				url: httpUrl() + "/backrole/batchAddRoleUrl",
				async: false,
				headers: {
					'accessToken': getToken()
				},
				contentType: 'application/json',
				data: JSON.stringify(urlRelations),
				success: function(res) {
					if(res.code == '0010') {
						getRoleUrl(tableId);
						roleTree();
						layui.notice.success("提示信息:分配成功!");
					} else {
						layui.notice.error(res.msg)
					}
				}
			});
			return false;
		}

	}
	//把角色里面的删除
	function getRolebox(tableId) {
		var checkedData = tree.getChecked('roleUrlTree'); //获取选中节点的数据
		console.log(checkedData.length)
		if(checkedData.length == 0) {
			layui.notice.warning("提示信息:请选择相关权限");
		} else {
			var children=[];
			for(var i=0;i<checkedData.length;i++){
				children.push(checkedData[i].children[0])
			}
			console.log(checkedData)
			var list = [];
			for(var i = 0; i < children.length; i++) {
				list.push({
					id: children[i].relationId
				})
			}
			var urlRelations = {
				'urlRelations': list
			}
			$.ajax({
				type: "post",
				url: httpUrl() + "/backrole/batchDeleteRoleUrl",
				async: false,
				headers: {
					'accessToken': getToken()
				},
				contentType: 'application/json',
				data: JSON.stringify(urlRelations),
				success: function(res) {
					if(res.code == '0010') {
						getURL();
						getRoleUrl(tableId);
						roleTree();
						layui.notice.success("提示信息:权限取消成功!");
					} else {
						layui.notice.error(res.msg)
					}
				}
			});
		}
	}
})