/**index_subtable.html代码模板*/

#sql("index_subtable")
#[[
#@layoutT("${tableComment}")
#define main()
	#@formStart()
		#@queryStart('关键词查询')
		   <input type="search" name="searchName" autocomplete="off" class="layui-input" placeholder="搜索关键词" style="padding-left:20px"/>
		   <i class="layui-icon layui-icon-search" style="position: absolute;top:12px;left:2px"></i>
      #@queryEnd()
	#@formEnd()
	
	<!-- 主表数据 -->
	<div class="layui-row  f-index-toolbar">
			<div class="layui-col-xs12">
   				<table id="maingrid" lay-filter="maingrid"></table>
   			</div>
   	</div>
   	   	
   	<!-- 表头工具栏 -->
   	<script type="text/html" id="table_toolbar">
  		<div class="layui-btn-group">
				<button class="layui-btn layui-btn-normal layui-btn-sm" lay-event="add">
				  <i class="layui-icon">&#xe608;</i> 新增
				</button>
				<button class="layui-btn layui-btn-normal layui-btn-sm" lay-event="update">
				  <i class="layui-icon">&#xe642;</i> 编辑
				</button>				
				<button class="layui-btn  layui-btn-normal layui-btn-sm" lay-event="delete">
				  <i class="layui-icon">&#xe640;</i> 删除
				</button>		
		</div>
	</script>
   	<!-- 	每行的操作按钮 -->
	<script type="text/html" id="bar_maingrid">
  		<a class="layui-btn layui-btn-xs" lay-event="edit">编辑</a>
  		<a class="layui-btn layui-btn-xs layui-btn-danger" lay-event="del">删除</a>
	</script>
	
	<!-- 从表Tab页签-->
	#@subTable([${subTableTitle}])
	
#end

#define js()
#@subTableJs()
<!-- 主表 -->
<script>
    gridArgs.title='${tableComment}';
    gridArgs.dataId='${primaryKey}';
    gridArgs.deleteUrl='#(path)${actionKey}/delete';
    gridArgs.updateUrl='#(path)${actionKey}/edit/';
    gridArgs.addUrl='#(path)${actionKey}/add';
    gridArgs.heightDiff=340;//调整表格高度
    gridArgs.gridDivId ='maingrid';
    initGrid({id : 'maingrid'
        ,elem : '#maingrid'
        ,toolbar:'#table_toolbar'
        ,cellMinWidth: 100
        ,cols : [ [
${tableCols}
            {fixed:'right',width : 180,align : 'left',toolbar : '#bar_maingrid'}
            ] ]
        ,url:"#(path)${actionKey}/list"
        ,searchForm : 'searchForm'
    });

   
</script>

<!-- 从表 -->
${subTableScript}

#end

<!-- 监听主表点击事件 --> 
#define layuiFunc()
  //监听行单击事件（双击事件为：rowDouble）
  table.on('row(maingrid)', function(obj){
    var data = obj.data;
    var refId=data.${refId};
    //渲染从表数据
    ${renderSubTable}
     //标注选中样式
    obj.tr.addClass('layui-table-click').siblings().removeClass('layui-table-click');
    
  });
#end
]]#
#end
