<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<div>
	<a class="easyui-linkbutton" onclick="importIndex()">导入商品到索引库</a>
</div>
<script type="text/javascript">

function importIndex() {
	$.post("/index/import",null,function(data){
		if(data.status == 200){
			$.messager.alert('提示','导入索引库成功！！！');
		}else{
			$.messager.alert('提示','导入索引库失败！！！');
		}
	});
}

</script>