<%@include file="../public/header.jsp"%>
<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
  <head>
    <base href="<%=basePath%>">

<title>数据模板页面</title>

<meta http-equiv="pragma" content="no-cache">
<meta http-equiv="cache-control" content="no-cache">
<meta http-equiv="expires" content="0">
<meta http-equiv="keywords" content="keyword1,keyword2,keyword3">
<meta http-equiv="description" content="This is my page">
<!--
	<link rel="stylesheet" type="text/css" href="styles.css">
	-->
<link href="${ctx}/static/admin/css/all.css" rel="stylesheet" type="text/css" />
<link href="${ctx}/static/admin/css/reset.css" rel="stylesheet" type="text/css" />

<script src="${ctx}/static/admin/js/jquery.js" type="text/javascript"></script>
<!-- <script src="${ctx}/static/admin/js/TreeSelector.js" type="text/javascript"></script> -->

	<link rel="stylesheet" href="${ctx }/static/ztree/css/demo.css" type="text/css">
	<link rel="stylesheet" href="${ctx }/static/ztree/css/zTreeStyle/zTreeStyle.css" type="text/css">
	<script type="text/javascript" src="${ctx }/static/ztree/js/jquery-1.4.4.min.js"></script>
	<script type="text/javascript" src="${ctx }/static/ztree/js/jquery.ztree.core-3.5.js"></script>
	<SCRIPT type="text/javascript">

		// 树形结构的数据在这里是写死的，实际应用中，是从数据库中读出的，存在request对象中，
		// 然后用request.getAttribute()方法，从一个变量中读出来，
		// 实际应用中应该是：var zNodes = request.getAttribute("data")，是从request中动态读出来的，
		// 关于如何拼接生成这种JSON嵌套格式的字符串，可以参考《附录一：多叉树结合JavaScript树形控件实现
		// 无限级树形结构（一种构建多级有序树形结构JSON（或XML）数据源的方法）》，
		// 《附录一》是专门介绍如何生成JSON嵌套格式的数据，
		// 注：下面的这个zNodes数据格式，我是在原有zTree标准数据格式的基础上扩展了一些属性，凡是以ex_开头
		// 的属性都是我扩展增加的，标准数据格式中没有，这些额外增加的属性，zTree是
		// 不会解析的，是我用来实现『按节点选择次数排序』和『搜索』功能时用到的，后台Java程序在构造数据时，
		// 需要构造成下面这个样子。
		// 
		// （注：扩展属性的含义：
		// catId：节点编号
		// pid：父节点编号
		// priority：节点选择次数（权值）
		// isHidden：节点可见性
		// ex_parentNode：父节点引用）
		
		var zNodes =  [${treeData}];		

		var setting = {
			callback: {
				onDblClick: clickNode
			}	
		};
		
		// 单击节点后触发的事件，
		// 在这个方法里，做两件事：
		//
		// 1、增加节点所在路径的权值（也叫做『路径向上加权』），这里的『路径向上加权』使用的方法与
		// 《附录二：新概念『智能树形菜单』--利用加权多叉树结合JavaScript树形控件实现》中的路径加权方法不一样，这是由于在JavaScript中无法建立
		// 像Java中的那种带有双向引用的多叉树结构（即父节点引用子节点，子节点引用父节点），在JavaScript中如果做这种双向引用的话，会造成
		// 『Stack overflow』异常，所以只能分别建立两棵多叉树对象，一棵是原始树形结构对象，另一棵是利用nodeMap建立的多叉树对象，专门用于
		// 反向引用，即子节点对父节点的引用。而在Java中，直接可以根据一个节点的父节点引用，找到它所有的父节点。但是在这里，只能采用一种笨
		// 办法，先从反向引用的多叉树中找到某一节点的所有父节点，存在一个数组里，然后在原始树形结构对象中使用先序遍历方法，从顶向下依次查找，
		// 把某一节点的所有父节点的权值加1，效率较低，但与利用反向引用查找父节点的方法目的是一样的。
		//
		// 2、更新节点选择次数到数据库中，以备下次登录系统时恢复原数据
		function clickNode(event, treeId, treeNode, clickFlag) {
		    var i=0;//记录是否有存在的项，如果有就不从左添加右框
			$("#seledCat option").each(function(index, element) {
	            if(treeNode.catId == $(this).val()){
					i++;
				}
	        });
		    if(i==0 &&(treeNode.children==undefined||treeNode.children.length==0)){
		       $("#seledCat").append('<option value="'+treeNode.catId+'">'+treeNode.name+'</option>');	
		    }
		}
		
	
	
		// 更新节点选择次数到数据库中，
		// 由于没有数据库，所以暂时不实现这个方法，实际应用时需要实现该方法
		function modifyNodeWeigthToDB() {
			// todo
		}
		
		// 增加节点所在路径的权值
		function increaseNodesWeight(node, parentNodes) {
			if (containNode(node, parentNodes)) {
				node.priority++;
			}
			if (node.children && node.children.length != 0) {
				var i = 0;
				for (; i < node.children.length; i++) {
						if (containNode(node, parentNodes)) {
							increaseNodesWeight((node.children)[i], parentNodes);
						}
				}
				// 如果在本层节点中没有找到要增加权值的节点，说明需要增加权值的节点都已经找完了，
				// 不需要再向下一层节点中寻找了，直接退出递归函数
				if (i == node.children.length - 1) {
					return;
				}
			}
		}

		// 排序方法：按照节点选择次数排序【冒泡法排序】
		// 节点选择次数大的排在前面，如果次数相等，按照编号排，编号小的排在前面
		function bubbleSortByWeight(theArray) {
				var temp;
		　　for (var i = 0; i < theArray.length-1; i++) { 		
			　　for (var j = theArray.length - 1; j > i ; j--) {		
				　　if (theArray[j].priority > theArray[j - 1].priority) {
				　　	temp = theArray[j];	
				　　	theArray[j] = theArray[j - 1];
				　　	theArray[j - 1] = temp;
				　　} else if (theArray[j].priority == theArray[j - 1].priority) {
							if (theArray[j].catId < theArray[j - 1].catId) {
								temp = theArray[j];	
					　　	theArray[j] = theArray[j - 1];
					　　	theArray[j - 1] = temp;
							}
						}
			　　}
		　　}
		}
		
		// 排序方法：按照节点编号排序，编号小的排在前面【冒泡法排序】
		function bubbleSortByUid(theArray) {
				var temp;
		　　for (var i = 0; i < theArray.length-1; i++) { 		
			　　for (var j = theArray.length - 1; j > i ; j--) {		
				　　if (theArray[j].catId < theArray[j - 1].catId) {
				　　	temp = theArray[j];	
				　　	theArray[j] = theArray[j - 1];
				　　	theArray[j - 1] = temp;
				　　}
			　　}
		　　}
		}

		// 按照节点选择次数对树形结构进行兄弟节点排序【递归排序】
		function orderSiblingsByWeight(node) {
			if (node.children && node.children.length != 0) {
				bubbleSortByWeight(node.children);
				for (var i = 0; i < node.children.length; i++) {
					orderSiblingsByWeight((node.children)[i]);
				}
			}
		}
		
		// 按照节点编号对树形结构进行兄弟节点排序【递归排序】
		function orderSiblingsByUid(node) {
			if (node.children && node.children.length != 0) {
				bubbleSortByUid(node.children);
				for (var i = 0; i < node.children.length; i++) {
					orderSiblingsByUid((node.children)[i]);
				}
			}
		}
		
		// 设置树节点为“不可见”状态【先序遍历法】
		function setTreeNotVisible(root) {
			root.isHidden = false;
			if (root.children && root.children.length != 0) {
				for (var i = 0; i < root.children.length; i++) {
						setTreeNotVisible((root.children)[i]);
				}
			}
		}
		
		// 设置树节点为“可见”状态【先序遍历法】
		function setTreeVisible(root) {
			root.isHidden = true;
			if (root.children && root.children.length != 0) {
				for (var i = 0; i < root.children.length; i++) {
						setTreeVisible((root.children)[i]);
				}
			}
		}
		
		// 设置当前节点及其所有上级节点为“可见”状态
		function setRouteVisible(root, node, nodeMap) {
			node.isHidden = true;
			var parentNodes = [];
			var currentNode = nodeMap['_' + node.catId];
			var parentNode = currentNode.ex_parentNode;	
			while (parentNode != null) {
				parentNodes.push(parentNode);			
				parentNode = parentNode.ex_parentNode;						
			}			
			// 如果没有上级节点，说明当前节点就是根节点，直接返回即可
			if (parentNodes.length == 0) {
				return;
			}			
			setParentNodesVisible(root, parentNodes);
		}
		
		// 设置所有上级节点为“可见”，
		// 这里的『设置上级节点为“可见”』使用的方法与《附录二：新概念『智能树形菜单』--利用加权多叉树结合JavaScript树形控件实现》
		// 中的『设置功能路径可见』方法不一样，这是由于在JavaScript中无法建立像Java中的那种带有双向引用的多叉树结构（即父节点
		// 引用子节点，子节点引用父节点），在JavaScript中如果做这种双向引用的话，会造成『Stack overflow』异常，所以只能分别建立
		// 两棵多叉树对象，一棵是原始树形结构对象，另一棵是利用nodeMap建立的多叉树对象，专门用于反向引用，即子节点对父节点的引用。
		// 而在Java中，直接可以根据一个节点的父节点引用，找到它所有的父节点。但是在这里，只能采用一种笨办法，先从反向引用的多叉树
		// 中找到某一节点的所有父节点，存在一个数组里，然后在原始树形结构对象中使用先序遍历方法，从顶向下依次查找，把某一节点的所有
		// 父节点设置为可见，效率较低，但与利用反向引用查找父节点的方法目的是一样的。
		function setParentNodesVisible(node, parentNodes) {
			if (containNode(node, parentNodes)) {
				node.isHidden = true;
			}
			if (node.children && node.children.length != 0) {
				var i = 0;
				for (; i < node.children.length; i++) {
						if (containNode(node, parentNodes)) {
							setParentNodesVisible((node.children)[i], parentNodes);
						}
				}
				// 如果在本层节点中没有找到要设置“可见性”的节点，说明需要设置“可见性”的节点都已经找完了，不需要再向下一层节点中寻找了，
				// 直接退出递归函数
				if (i == node.children.length - 1) {
					return;
				}
			}
		}
		
		// 检查数组中是否包含与指定节点编号相同的节点
		function containNode(node, parentNodes) {
			for (var i = 0; i < parentNodes.length; i++) {
				if (parentNodes[i].catId == node.catId) {
					return true;
				}
			}
			return false;
		}
		
		// 搜索包含关键字的树节点，将包含关键字的节点所在路径设置为“可见”，例如：如果某一节点包含搜索关键字，
		// 那么它的所有上级节点和所有下级节点都设置为“可见”【先序遍历法】
		function searchTreeNode(root1, root2, nodeMap, keyWord) {
			if (root2.name.indexOf(keyWord) > -1) {
				setTreeVisible(root2);
				setRouteVisible(root1, root2, nodeMap);
			} else {
				if (root2.children && root2.children.length != 0) {
					for (var i = 0; i < root2.children.length; i++) {
						searchTreeNode(root1, (root2.children)[i], nodeMap, keyWord);
					}
				}
			}
		}
		
		// 将原树形结构数据复制出一个副本，以备对副本进行搜索过滤，而不破坏原始数据（原始数据用来恢复原状用）【先序遍历法】
		function cloneTreeNodes(root) {
			var treeJSON = '{' + 'name : \'' + root.name + '\', catId : \'' + root.catId + '\',' + 'pid : \'' + root.pid + '\',' + ' priority : ' + root.priority + ', isHidden : true, ex_parentNode : null';
			if (root.children && root.children.length != 0) {
				treeJSON += ', children : [';
				for (var i = 0; i < root.children.length; i++) {
					treeJSON += cloneTreeNodes((root.children)[i]) + ',';
				}
				treeJSON = treeJSON.substring(0, treeJSON.length - 1);
				treeJSON += "]";
			}
			return treeJSON + '}';
		}
		
		// 构造节点映射表【先序遍历法】
		// 这里特殊说明一下：
		// 构造节点映射表的目的，是为了下面建立子节点对父节点的引用，这是一个中间步骤，但是有个小问题：
		// 在javascript中，如果是在原树状对象上建立子节点对父节点的引用，会发生『Stack overflow』错误，
		// 我估计是由于循环引用造成的，因为原树状对象已经存在父节点对子节点的引用，此时再建立子节点对
		// 父节点的引用，造成循环引用，这在Java中是没有问题的，但是在JavaScript中却有问题，所以为了避免
		// 这个问题，我创建了一批新的节点，这些节点的内容和原树状结构节点内容一致，但是没有children属性，
		// 也就是没有父节点对子节点的引用，然后对这批新节点建立子节点对父节点的引用关系，这个方法会被buildParentRef()方法调用，来完成这个目的。
		function buildNodeMap(node, nodeMap) {
		  var newObj = new Object();
		  newObj.name = node.name;
		  newObj.catId = node.catId;
		  newObj.pid = node.pid;
		  newObj.priority = node.priority;
		  newObj.isHidden = node.isHidden;
			nodeMap['_' + node.catId] = newObj;
			if (node.children && node.children.length != 0) {
				for (var i = 0; i < node.children.length; i++) {
					buildNodeMap((node.children)[i], nodeMap);
				}
			}
			return nodeMap; // 这里需要将nodeMap返回去，然后传给buildParentRef()函数使用，这和Java中的引用传递不一样，怪异！！
		}
		
		// 建立子节点对父节点的引用
		function buildParentRef(node, nodeMap) {
			for (catId in nodeMap) {
				if ((nodeMap[catId]).pid == '') {
					(nodeMap[catId]).ex_parentNode = null;
				} else {
					(nodeMap[catId]).ex_parentNode = nodeMap['_' + (nodeMap[catId]).pid];
				}
			}
			return nodeMap;
		}
		
		// 对树形结构数据进行搜索过滤后，根据JavaScript树状对象，重新生成JSON字符串【先序遍历法】
		function reBuildTreeJSON(node) {
			if (node.isHidden) {
				var treeJSON = '{' + 'name : \'' + node.name + '\', catId : \'' + node.catId + '\',' + 'pid : \'' + node.pid + '\',' + ' priority : ' + node.priority + ', isHidden : ' + node.isHidden + ', ex_parentNode : null';
				if (node.children && node.children.length != 0) {
					treeJSON += ', children : [';
					for (var i = 0; i < node.children.length; i++) {
						if ((node.children)[i].isHidden) {
							treeJSON += reBuildTreeJSON((node.children)[i]) + ',';
						} else {
							treeJSON += reBuildTreeJSON((node.children)[i]);
						}
					}
					treeJSON = treeJSON.substring(0, treeJSON.length - 1);
					treeJSON += "]";
				}
				return treeJSON + '}';
			} else {
				return '';
			}
		}
		
		// 树形结构搜索
		function searchTreeNodesByKeyWord() {
			// 声明一个新的树对象
			var newZNodes = null;
			// 将原树形结构恢复默认状态
			orderSiblingsByUid(zNodes[0]);
			// 将原树对象复制出一个副本，并将这个副本JSON字符串转换成新的树对象
			var treeJSON = cloneTreeNodes(zNodes[0]);
			newZNodes = eval('(' + '[' + treeJSON + ']' + ')');
			
			var root = newZNodes[0];
			// 对新树对象建立反向引用关系（在子节点中增加父节点的引用）
			var nodeMap = {};
			// 构造节点映射表（下面借助该映射表建立反向引用关系）
			nodeMap = buildNodeMap(root, nodeMap);
			// 建立子节点对父节点的引用
			nodeMap = buildParentRef(root, nodeMap);
			// 设置树节点为“不可见”状态
			setTreeNotVisible(root);
			// 搜索包含关键字的树节点，将包含关键字的节点所在路径设置为“可见”，例如：如果某一节点包含搜索关键字，
			// 那么它的所有上级节点和所有下级节点都设置为“可见”
			searchTreeNode(root, root, nodeMap, document.getElementById('search').value);			
			// 对树形结构数据进行搜索过滤后，根据JavaScript树状对象，重新生成JSON字符串
			treeJSON = reBuildTreeJSON(root);
			newZNodes = eval('(' + '[' + treeJSON + ']' + ')');
			
			$.fn.zTree.init($("#treeDemo"), setting, newZNodes);			
			$.fn.zTree.getZTreeObj("treeDemo").expandAll(true);
		}

		// 按照节点选择次数排序（按选择次数排序）
		function orderByWeight() {
			orderSiblingsByWeight(zNodes[0]);	
			$.fn.zTree.init($("#treeDemo"), setting, zNodes);
			$.fn.zTree.getZTreeObj("treeDemo").expandAll(true);		
			document.getElementById('search').value = '';
		}

		// 按照节点编号排序（恢复默认状态）
		function orderByUid() {
			orderSiblingsByUid(zNodes[0]);
			$.fn.zTree.init($("#treeDemo"), setting, zNodes);		
			$.fn.zTree.getZTreeObj("treeDemo").expandAll(true);
			document.getElementById('search').value = '';	
		}

		//============建立原始树状对象节点的反向引用关系，即子节点对父节点的引用=============//
		//============单击树节点，进行路径向上加权时会用到===================================//
		// 这里的nodeMap是全局变量，和searchTreeNodesByKeyWord()方法中的nodeMap变量作用域不一样
		var nodeMap = {};
		// 构造节点映射表（下面借助该映射表建立反向引用关系）
		nodeMap = buildNodeMap(zNodes[0], nodeMap);
		// 建立子节点对父节点的引用
		buildParentRef(zNodes[0], nodeMap);
		//===============================================================================//
		
		// 初始化该树形结构，默认展开所有节点
		$(document).ready(function(){
			orderSiblingsByUid(zNodes[0]);
			$.fn.zTree.init($("#treeDemo"), setting, zNodes);
			$.fn.zTree.getZTreeObj("treeDemo").expandAll(true);
		});
		
	 $(function(){
	    var arrseledcat = [],$tbmain = $("#tbmain");
	
		$(".dodel").click(function(){
			if(confirm('是否确定不发布该数据?')){
				$(this).parent().parent().remove();
			}			
		});
        
		$("#seledCat").dblclick(function(){
			var $this=$(this),val = $('option:selected',this).val();//,index = $.inArray(val,arrseledcat);
			//arrseledcat.splice(index,1);
			$('option:selected',this).remove();
	    });
	    
		$("#publish").click(function(){
		    $("#seledCat option").each(function(index, element) {
				arrseledcat.push($(this).val());
            });
			if(arrseledcat.length==0){
				alert('请至少选择一个栏目。');
				return false;
			}
			var arralldata = [];
			$tbmain.find(":text").each(function(){
				//alert($(this).attr('id'));
				var $this= $(this),id=$this.data("id"),ctid=$this.data("contentid"),title = this.value?this.value:$this.parent().siblings(".oldtitle").text(),arrdata=[];
				arrdata = [id,title,ctid];
				arralldata.push(arrdata);
			});
			if(arralldata.length){
				$.ajax({
					url:"${ctx}/datasource/publish.do?csrfToken=${csrfToken}",
					type:"POST",
					data:{type:'${datatype}',ctype:arrseledcat.join(","),alldata:arralldata.join(";")},
					dataType:"json",
					async:false,
					success:function(data){
							if(data.status=="1") {
							    alert("数据发布成功");
								window.location.href = '${ctx}/'+data.backurl;//需要恢复
							}else{
								window.location.reload();	//需要恢复
							}
					},
					error:function(){}					
				});
			}else{
				alert('发布的数据不存在。');
				return false;			
			}
		}); 
	});
 
	</script>
</head>

<body style="background-color: white">
	<h2 class="adm-title clearfix"> <a class="fl title">源数据管理</a> </h2>
	<div class="pad10">
		<form id="fm_es" method="post" action="">
		    <input type="hidden" name="csrfToken" value="${csrfToken}"/>
		    <div class="content_wrap" style="height: 270px;">
		    <div class="zTreeDemoBackground left">
		     <ul class="info">
				<li><input type="text" value="" style="width: 200px;" size="16" id="search">&nbsp;&nbsp;&nbsp;&nbsp;<input value="搜索" type="button" onclick="searchTreeNodesByKeyWord()" />&nbsp;&nbsp;<input value="恢复默认" type="button" onclick="orderByUid()" /> </li>
		    </ul>
		    <ul id="treeDemo" class="ztree"></ul>
		    </div>
			选择发布
			<select multiple="multiple" id="seledCat" style="height:253px;width:300px;max-height:300px; min-height:200px;max-width:500px; min-width:300px;">
			</select>
			<span class="adm-quence"><a href="javascript:;" id="publish" data-id="all" >批量发布</a></span>
<!-- 			<div class="adm-quence"> -->
<!-- 				<a href="javascript:;" id="publish" data-id="all">批量发布</a> -->
<!-- 			</div> -->
			</div>
			<table class="adm-table">
				<thead>
					<tr class="tr1">
						<td width="150">数据id</td>
						<td width="35%">数据原标题</td>
						<td width="35%">数据新标题</td>
						<td>操作</td>
					</tr>
				</thead>
				<tbody id="tbmain" >
					<c:forEach items="${dlist }" var="vo">
				    <tr>
				      <td>${vo.contentid}</td>
				      <td class="oldtitle">${vo.title}</td>
				      <td><input type="text" id="inp_${vo.dataid}" data-id="${vo.dataid}" data-contentid="${vo.contentid}" style="width:350px;height: 18px;" /></td>
				      <td><a href="javascript:;" class="dodel">删除</a></td>
				    </tr>
				  </c:forEach>
				</tbody>
			</table>
			<div class="adm-page"> </div>
		</form>
	</div>

	

</body>
</html>
