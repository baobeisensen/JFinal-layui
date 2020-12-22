# Jfinal-layui-pro 专业版

#### 介绍
JFinal+layui极速开发企业应用管理系统，是以JFinal+layui为核心的企业应用项目架构，利用JFinal的特性与layui完美结合，达到快速启动项目的目的。让开发更简单高效，即使你不会layui，也能轻松掌握使用。该项目的核心功能有：登录、功能管理、角色管理（包含了权限管理）、用户管理、部门管理、系统日志、业务字典，报表管理、代码生成器、通用的附件上传、下载、导入、导出，echart图表统计，缓存，druid的sql监控，基本满足企业应用管理系统的需求，简化了前段代码，后台公用接口都封装完善，你只需要开发业务功能即可。从后端架构到前端开发，从开发到部署，这真正的展现了jfinal极速开发的魅力。

 

### pro和master版本的区别主要是前端界面的不同，在原有的基础上修改css，调整首页布局，打造一款美观、好用、专业的后台管理系统


#### 软件架构
软件架构说明：
核心架构：[jfinal](http://www.jfinal.com)，[jfinal-undertow](http://www.jfinal.com/doc/1-4)，[layui](https://www.layui.com/)，mysql，ehcach,rsa加密算法
系统权限:通过“用户-角色-功能”三者关系来实现系统的权限控制，操作简单明了，代码实现极其简单，完全可以替代shiro，你不用再去折腾shiro那一套了，这都是得益于jfinal架构的巧妙设计。
前端页面：封装了layui常用模块代码，参照使用例子，就能快速上手，无需担心不懂layui。
系统日志：操作日志、数据日志、登录日志，无需注解和手动添加，就能跟踪记录数据


#### 友情链接

 **JFinal-layui在线演示系统：** [JFinal极速开发企业应用管理系统](http://www.qinhaisenlin.com:8080/)

 **JFinal-layui交流群：970045838**

 **[视频教程-入门实战](https://www.qinhaisenlin.com/share/47   )

#### 安装教程

1. 新建数据库,执行doc目录下的jfinal-layui.sql
2. 修改resources下面的config-dev.txt配置文件,修改数据库配置
3. 运行com.qinhailin.common.config.ManiConfig的main方法
4. 访问地址：http://localhost;登录账号：admin/123456

#### 使用说明
jfinal的通用配置如果不是特别需要，不需要修改，直接开发你的功能即可

1. controller控制类：只需继承BaseController就能拥有上传、导入、导出等通用方法。
   ControllerBind的path、viewPath默认相同，也可自定义：

```
@Path("/portal/core/sysUser")
public class SysUserController extends BaseController {
	@Inject
	SysUserService service;

	public void index() {
		setAttr("orgList", service.queryOrgIdAndNameRecord());
		render("index.html");
	}

	public void list() {
            renderJson(service.page(getParaToInt("pageNumber", 1), getParaToInt("pageSize", 10), getAllParamsToRecord()));
	}
 }
```

2. service服务类：只需要继承BaseService接口，实现getDao()方法，就能拥有对数据库持久层的所有方法接口。
  
```
   public class SysUserService extends BaseService {

	private SysUser dao = new SysUser().dao();
	
	@Override
	public Model<?> getDao(){
		return dao;
	}

    public Grid page(int pageNumber, int pageSize, Record record) {
		Record rd = new Record();
		rd.set("a.user_code like", record.getStr("userCode"));
		rd.set("a.user_name like", record.getStr("userName"));
		rd.set("a.sex=", record.getStr("sex"));
		String sql=Db.getSql("core.getUserList");
		String orgId=record.getStr("orgId");
		
		//部门用户列表
		String type=record.getStr("type");
		if("org".equals(type)){
			
			StringBuffer sbf=new StringBuffer();
			sbf.append("'").append(orgId).append("'");		
			String orgIds=orgService.getIdsByOrgId(orgId,sbf);
			
			sql=Db.getSql("core.getOrgUserList").replace("?", orgIds);
			return queryForList(sql,pageNumber, pageSize, rd, null);			
		}
		//用户管理列表
		rd.set("a.org_id=", orgId);
		return queryForList(sql,pageNumber, pageSize, rd, null);
	}
  }

```

3. 前端页面，封装了layui常用代码，添加修改页面使用函数#@colStart和#@colEnd即可,#@colStart和#@colEnd必须成对出现
  
```
   <div class="layui-row layui-col-space1 task-row">
	#@colStart('用户编号',6)		
	   <input type="text" class="layui-input" name="sysUser.userCode" value="#(sysUser.user_code??)" 
		lay-verType='tips'lay-verify="required|" maxlength="50" placeHolder="必填"/>
	#@colEnd()
		
	#@colStart('密码',6)
	    <input type="password" class="layui-input" name="sysUser.passwd" value="#(sysUser.passwd??)"
		lay-verType='tips'lay-verify=""  maxlength="50" placeHolder="不填则使用默认密码"/>
	#@colEnd()
    </div>

```

4、分页列表，页面代码也极其简单明了

```
<script>
    //自定义弹窗
	 function userRole(obj){
		 var data=obj.data;
		var userCode=data.user_code;
		var userName=data.user_name;
		var url="#(path)/portal/core/sysUser/userRole?userCode="+userCode+"&userName="+userName;
		openDialog("配置用户角色",url,false,null,null);
	 }
	//分页表格参数
	gridArgs.title='功能';
	gridArgs.dataId='id';
	gridArgs.deleteUrl='#(path)/portal/core/sysUser/delete';
	gridArgs.updateUrl='#(path)/portal/core/sysUser/edit/';
	gridArgs.addUrl='#(path)/portal/core/sysUser/add';
	gridArgs.resetUrl='#(path)/portal/core/sysUser/resetPassword';
	gridArgs.gridDivId ='maingrid';
  gridArgs.heightDiff=82;//调整表格高度
	initGrid({id : 'maingrid'
			,elem : '#maingrid'
			,cellMinWidth: 80
             ,toolbar:'#table_toolbar'//自定义工具栏		
			,cols : [ [
					{title: '主键',field : 'id',width : 35,checkbox : true},						
					{title:'序号',type:'numbers',width:35},
					{title: '用户名', field: 'user_code' },
        			{title: '姓名', field: 'user_name'},
	        		{title: '所属部门', field: 'org_name'},
	        		{title: '性别', field: 'sex',templet:'#sexStr'},
        			{title: '电话', field: 'tel'},
        			{title: '手机号码', field: 'mobile'},
        			{title: '邮箱', field: 'email'},
        			{title: '允许登录', field: 'allow_login',templet:'#numToStr' },																		
					{fixed:'right',width : 180,align : 'left',toolbar : '#bar_maingrid'} // 这里的toolbar值是模板元素的选择器
			] ]
			,url:"#(path)/portal/core/sysUser/list"
			,searchForm : 'searchForm'
		},{role:userRole});
	
</script>

<script type="text/html" id="sexStr">
    {{ d.sex == 1 ? '男' : '女' }}             
</script>
<script type="text/html" id="numToStr">
    <input type="checkbox" name="isStop" {{(d.id=='admin'||d.id=='superadmin')?'disabled':''}} value="{{d.id}}" 
		lay-skin="switch" lay-text="是|否" lay-filter="allowLoginFilter" {{ d.allow_login == 0 ? 'checked' : '' }}>               
</script>
```
5、业务字典快速引用函数
**#@getSelect(code,name,text)** ； **#@getRadio(code,name,text)** ；**#@getCheckbox(code,name,text)** 
code:字典编号，name:元素name属性,text:选项名称，需要选中值，在调用之前设置值即可：#set(value='选中值')，如：系统日志类型引用：
```
   #@queryStart('日志类型')					
	#@getSelect('logType','remark','日志类型')			
   #@queryEnd() 
   #@queryStart('日志类型')
        #set(value='数据日志')					
	#@getRadio('logType','remark','日志类型')			
   #@queryEnd() 
   #@queryStart('日志类型')
        #set(value='操作日志,数据日志,登录日志')					
	#@getCheckbox('logType','remark','日志类型')			
   #@queryEnd() 
```
![业务字典快速引用](https://images.gitee.com/uploads/images/2019/0107/190356_a4e5ac71_1692092.png "日志类型快速引用实例.png")


#### 系统界面
1、登录界面，第一次不显示验证码，输错一次密码，则需要验证码
![第一次登录界面](https://images.gitee.com/uploads/images/2020/1217/174600_440635b1_1692092.png "登录登录.png")

![密码错误，显示验证码](https://images.gitee.com/uploads/images/2020/1217/174703_205ef05a_1692092.png "显示验证码.png")
2、登录后的管理主页
![管理主页](https://images.gitee.com/uploads/images/2020/1222/205352_9487c450_1692092.png "系统管理主页.png")
3、系统管理核心模块
![功能管理](https://images.gitee.com/uploads/images/2020/1222/205555_32261dfa_1692092.png "功能管理.png")
![角色管理](https://images.gitee.com/uploads/images/2020/1222/205651_fe52e54d_1692092.png "角色管理.png")
![用户管理](https://images.gitee.com/uploads/images/2020/1222/205810_3dbc44b1_1692092.png "用户管理.png")
![部门管理](https://images.gitee.com/uploads/images/2020/1222/210053_ace40f91_1692092.png "部门管理.png")
![业务字典](https://images.gitee.com/uploads/images/2020/1222/210310_7dac4ab5_1692092.png "业务字典.png")
![系统日志](https://images.gitee.com/uploads/images/2020/1222/210349_446b56e5_1692092.png "系统日志.png")
![自定义SQL](https://images.gitee.com/uploads/images/2020/1222/210432_96eaa6a8_1692092.png "自定义sql.png")
![附件上传](https://images.gitee.com/uploads/images/2020/1217/160018_b02f38be_1692092.png "附件上传.png")
![附件列表](https://images.gitee.com/uploads/images/2020/1217/160059_447aacad_1692092.png "附件列表.png")
![echart图表](https://images.gitee.com/uploads/images/2020/1217/160140_a64d5839_1692092.png "echart图表.png")
![单表代码生成器](https://images.gitee.com/uploads/images/2020/1222/210914_44c91768_1692092.png "单表代码生成器.png")
![主从表代码生成器](https://images.gitee.com/uploads/images/2020/1217/160317_55b40123_1692092.png "主从表代码生成器.png")
![主从表示例](https://images.gitee.com/uploads/images/2020/1217/160810_05683f52_1692092.png "主从表示例.png")
![报表设计器](https://images.gitee.com/uploads/images/2020/1217/160913_3f056759_1692092.png "报表设计器.png")
![报表预览](https://images.gitee.com/uploads/images/2020/1217/160958_f6997107_1692092.png "报表预览.png")
![可编辑表格](https://images.gitee.com/uploads/images/2020/1217/161043_2b869643_1692092.png "可编辑表格.png")
![联级多选](https://images.gitee.com/uploads/images/2020/1217/161123_9f1cb41e_1692092.png "联级多选.png")
 **4、响应式布局展示：** 

![移动端主菜单](https://images.gitee.com/uploads/images/2020/1222/211404_034d7e13_1692092.png "移动端菜单.png")
![功能管理菜单树](https://images.gitee.com/uploads/images/2020/1222/211445_16e2dfd0_1692092.png "功能管理菜单树.png")
![功能管理列表](https://images.gitee.com/uploads/images/2020/1222/211616_9bf6a1e5_1692092.png "功能管理列表.png")
![功能管理添加弹窗](https://images.gitee.com/uploads/images/2020/1222/211652_f0cb6d53_1692092.png "功能管理添加弹窗.png")

![用户管理列表](https://images.gitee.com/uploads/images/2020/1222/211941_93cff64b_1692092.png "用户管理列表.png")
![删除提示](https://images.gitee.com/uploads/images/2020/1222/212333_7f747c6f_1692092.png "删除提示.png")

感兴趣的攻城狮可以参考，希望能对你有帮助。