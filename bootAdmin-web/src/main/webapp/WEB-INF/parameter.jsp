<%@ page contentType="text/html;charset=UTF-8"%>
<html>
<head>
<title>配置参数</title>
<meta name="decorator" content="blank" />
<link rel="stylesheet" type="text/css" href="/static/css/bootstrap.css" />
<link rel="stylesheet" type="text/css" href="/static/css/tools.css" />
<link rel="stylesheet" type="text/css" href="/static/layer2.1/skin/layer.css" />
<script src="/static/js/jquery-1.9.1.js" type="text/javascript"></script>
<script src="/static/js/bootstrap.js" type="text/javascript"></script>
<script src="/static/layer2.1/skin/layer.js" type="text/javascript"></script>

<script type="text/javascript">

	$(function(){
		select_model();
	});
	
	function select_model(){
		$.ajax({
			url : 'getMessage',
			type : 'post',
			data : $('#logform').serialize(),
			success : function(data) {
				if (data.data) {
					var tbody="";
					for(var d in data.data){
						tbody+="<tr><td>"+data.data[d].id+"</td><td>"+data.data[d].version+"</td><td><button type='button' class='btn btn-primary' onclick='getValueByid("+data.data[d].id+")'>详细</button>&nbsp;&nbsp;<button type='button' class='btn btn-primary' onclick='delete_model("+data.data[d].id+")'>删除</button></td></tr>";
					}
					$("#tbody").html(tbody);
				}
			}
		});
	}

	function getValueByid(id){
		console.log(id);
		$.ajax({
			url : 'getValueByid',
			type : 'post',
			data : {
				id:id
			},
			success : function(data) {
				$('#myModal').modal('show');
				$("#u_id").val(id);
				$("#u_version").val(data.version);
				$("#u_value").val(data.value);
			}
		});
	}
	
	function open_model(){
		$("#u_id").val('');
		$("#u_version").val('');
		$("#u_value").val('');
		$('#myModal').modal('show');
	}
	
	function close_model(){
		$("#u_id").val('');
		$("#u_version").val('');
		$("#u_value").val('');
	}
	
	function save_model(){
		$.ajax({
			url : 'saveValue',
			type : 'post',
			data : {
				id:$("#u_id").val(),
				version:$("#u_version").val(),
				value:$("#u_value").val()
			},
			success : function(data) {
				if(data.code == "200"){
					alert(data.msg);
					$("#u_version").val('');
					$("#u_value").text('');
					$('#myModal').modal('hide');
					select_model();
					/* layer.msg("保存成功！"); */
				}else{
					/* layer.msg("保存失败！"); */
					alert(data.msg);
				}
			}
		});
	}
	
	function delete_model(d_id){
		console.log(d_id+"_");
		$.ajax({
			url : 'deleteValue',
			type : 'post',
			data : {
				id:d_id
			},
			success : function(data) {
				console.log(d_id+"  dd_");
				if(data == "200"){
					/* layer.msg("删除成功！"); */
					alert("删除成功！");
					select_model();
				}else{
					/* layer.msg("删除失败！"); */
					alert("删除失败！");
				}
			}
		});
	}

	

</script>
</head>
<body style="margin-top:4%;">
	<form id="logform" action="" method="post">
		<input type="hidden" name="pageSize" value="10" />
		<input type="hidden" name="currentPage" value="1" />
		<input type="text" style="display:initial; width:200px;" name="version" class="form-control" placeholder="版本号"/>
		<button type="button" class="btn btn-primary" onclick="select_model();">查询</button>
		<button type="button" class="btn btn-primary" onclick="open_model();">新增</button>
	</form>
	<div style="width:90%;padding-left:5%;">
		<table id="contentTable" class="table table-striped table-bordered table-condensed">
			<thead>
				<tr>
					<th class="th">ID</th>
					<th class="th">版本号</th>
					<th class="th">操作</th>
				</tr>
			</thead>
			<tbody id="tbody">
			</tbody>
		</table>
	</div>
<div class="modal fade" id="myModal" tabindex="-1" role="dialog" aria-labelledby="myModalLabel" aria-hidden="true">
	<div class="modal-dialog">
		<div class="modal-content">
			<div class="modal-header">
				<h5 class="modal-title" id="myModalLabel">配置信息</h5>
			</div>
			<div class="modal-body">
				<input type="hidden" id="u_id">
				<label class="input-label" style="float:left; padding-top:10px;">版本号：</label> 
			    <input type="text" id="u_version" class="form-control" style="float:left; display:initial; width:200px;" />
			    <br/><br/><br/>
			    <label class="input-label" style="float:left;">配置信息：</label> 
				<textarea id="u_value" rows="15" cols="70" class="form-control" ></textarea>
			</div>
			<div class="modal-footer">
				<button type="button" class="btn btn-default" data-dismiss="modal" onclick="close_model();">关闭</button>
				<button type="button" class="btn btn-primary" onclick="save_model();">保存</button>
			</div>
		</div>
	</div>
</div>
	
</body>
</html>