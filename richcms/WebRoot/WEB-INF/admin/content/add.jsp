<%@include file="../public/header.jsp"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!doctype html public "-/w3c/dtd html 4.01 transitional/en" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html>
	<head>
		<meta name="keywords" content=" " />
		<meta name="description" content="" />
		<meta http-equiv="pragma" content="no-cache">
		<meta http-equiv="cache-control" content="no-cache">
		<meta http-equiv="expires" content="0">  
		<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
		<title>栏目内容管理</title>
		<link href="${ctx}/static/admin/css/all.css" rel="stylesheet" type="text/css" />
		<link href="${ctx}/static/admin/css/reset.css" rel="stylesheet" type="text/css" />
		<link href="${ctx}/static/admin/css/validform.css" rel="stylesheet" type="text/css" />
		<link href="${ctx}/static/admin/css/uploadify.css" rel="stylesheet"	type="text/css" />
		<link href="${ctx}/static/admin/css/ligerui-icons.css" rel="stylesheet"	type="text/css" />
		<link href="${ctx}/static/admin/css/ligerui-all.css" rel="stylesheet"	type="text/css" />
		
		<script type="text/javascript" src="${ctx}/static/admin/js/jquery.js"></script>
		<script type="text/javascript" src="${ctx}/static/admin/js/json2.js"></script>
		<script type="text/javascript" src="${ctx}/static/admin/js/uploadify/jquery.uploadify.js"></script>
		<script type="text/javascript" src="${ctx}/static/admin/js/Validform.js"></script>
		<script type="text/javascript" src="${ctx}/static/admin/js/ligerui/base.js"></script>
		<script type="text/javascript" src="${ctx}/static/admin/js/ligerui/plugins/ligerCheckBox.js"></script>
    	<script type="text/javascript" src="${ctx}/static/admin/js/ligerui/plugins/ligerResizable.js"></script>
    	<script type="text/javascript" src="${ctx}/static/admin/js/ligerui/plugins/ligerComboBox.js"></script>
    	<script type="text/javascript" src="${ctx}/static/admin/js/ligerui/plugins/ligerDateEditor.js"></script>
		<script src="${ctx}/static/admin/js/TreeSelector.js" type="text/javascript"></script>
		<style type="text/css">
			.display-image{height: 120px;width: 120px;margin: 2px;border: 1px dashed gray;cursor: hand;float: left;margin-bottom: 5px;}
			.display-image-check{height: 120px;width: 120px;margin: 2px;border: 1px solid red;cursor: hand;float: left;margin-bottom: 5px;}
		</style>
		

	</head>
	<body style="overflow-x: hidden; padding: 4px;">
		<h2 class="adm-title">
			<a class="title">栏目内容管理</a>
		</h2>
		<div class="pad10">
			<div class="adm-text ul-float">
				<ul class="clearfix">
					<li>
						<span class="mr20"></span>
						<input type="radio" class="adm-radio mr5" name="publishtype" />
						单条发布
						<input type="radio" class="adm-radio mr5" name="publishtype" />
						批量发布
					</li>
					<li>
						<span class="mr20 m">栏目：</span>
						<span class="mr20">全部软件</span><span
							style="color: #219b00; width: 400px; position: relative;"><a
							href="javascript:;" id="toother">【同时发布到其他栏目】</a>
						<div style="position: absolute; display: none;" id="cattree"></div>
						</span>
					</li>
					<li>
						<span class="mr20 m">标题：</span>
						<input class="l mr20" type="text" value="" />
						<span class="mr20" style="color: red;">如果选择多个数据或者没有标题，则原始数据的标题为文字头条。</span>
					</li>
					<li>
						<span class="mr20 m">数据类型：</span>
						<select style="width: 184px; padding: 0;">
							<volist name="datasc" id="vo">
							<option value="{$vo['srcid']}_{$vo['source']}">
								{$vo['datatype']}
							</option>
							</volist>
						</select>
					</li>
					<li>
						<span class="mr20 m">数据编号：</span>
						<input type="button" class="adm-btn" id="selcontent"
							value="选择对应的数据" />
					</li>
					<if condition="$id">
					<li>
						<span class="mr20 m"></span>
						<table>
							<thead>
								<tr>
									<th width="60">
										数据id
									</th>
									<th width="420">
										数据标题
									</th>
								</tr>
							</thead>
							<tbody>
								<tr>
									<td>
										1212
									</td>
									<td>
										sdfasdsdfasdf
									</td>
								</tr>
							</tbody>
						</table>
					</li>
					</if>
					<li>
						<span class="mr20 m">选择图片：</span>
						<input class="l mr20" type="text" value="" />
						<input type="button" class="adm-btn" value="上传" />
					</li>
					<li>
						<span class="mr20 m">是否打水印：</span>
						<input type="radio" class="adm-radio mr5" />
						<span class="mr20">打水印</span>
						<input type="radio" class="adm-radio mr5" />
						不打水印
					</li>
					<li>
						<span class="mr20 m">水印文字：</span>
						<input class="l mr20" type="text" value="" />
					</li>
					<li>
						<span class="mr20 m">水印位置：</span>
						<select style="width: 184px; padding: 0;">
							<option>
								上
							</option>
						</select>
					</li>
					<li>
						<span class="mr20 m">图片提示语：</span>
						<input class="l mr20" type="text" value="" />
					</li>
					<div class="center">
						<img class="mb20" style="margin: 0 auto" src="images/300x200.png"
							alt="" />
					</div>
				</ul>
			</div>
		</div>
		<div class="adm-btn-big">
			<a href="">提&nbsp;&nbsp;交</a>
		</div>

		<div class="float-box jqmWindow" id="flcontent">
			<div class="adm-text">
				<ul class="clearfix">
					<li>
						<span class="mr5 m">标题：</span>
						<input class="mr10" type="text" name="" value="输入文字">
					</li>
					<li>
						<span class="mr5 m">开始时间：</span>
						<input class="mr10" type="text" name="" value="输入文字">
						<a href="" class="adm-add-btn">搜索</a>
					</li>
				</ul>
			</div>

			<table class="adm-table mt20">
				<tbody>
					<tr class="tr1">
						<td>
							<input type="checkbox" class="adm-chk">
							全选
						</td>
						<td>
							数据类型
						</td>
						<td>
							数据ID
						</td>
						<td>
							数据标题
						</td>
					</tr>
					<tr class="tr">
						<td>
							<input type="checkbox" class="adm-chk">
						</td>
						<td>
							应用
						</td>
						<td>
							1
						</td>
						<td>
							风怒的飞秒
						</td>
					</tr>
					<tr class="tr2">
						<td>
							<input type="checkbox" class="adm-chk">
						</td>
						<td>
							应用
						</td>
						<td>
							2
						</td>
						<td>
							扑街玛丽
						</td>
					</tr>
				</tbody>
			</table>
			<div class="adm-btn-big mt30">
				<a href="#">确&nbsp;&nbsp;定</a>
			</div>
		</div>
	</body>
</html>