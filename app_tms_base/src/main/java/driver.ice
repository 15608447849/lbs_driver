[["java:package:com.hsf.framework.api"]]



/**
 * 司机管理服务
 */
 module driver{
 const string scanTag = "fspace";
 /**
 0： 已发布 A公司发单
 1： 已抢单 B公司抢单
 2： 已中转 B-1司机把货物放入中转站卸货
 3:  已取货 B-1司机去A公司取货
 4:  已签收 B-1司机成功送达,对方已签收
 5： 纠纷中
 6:  待评价
 7:  抢单成功
 8： 已完成
 9:  已卸货
 20: 取消发布
 */

 /**已抢单 可以获取待取货列表 , 成功取货后改变状态为 已取货*/
 const int sGRAB = 7;
 /**已取货 可以获取待运输列表 , 成功签收后改变状态为 已签收,途中货物卸货停止轨迹,当被其他司机扫码取货后,变为已取货状态*/
 const int sCLAIM = 3;
 /**已签收 可以获取已完成列表*/
 const int sSIGN = 4;

 /**已卸货*/
 const int sUnload = 9;
 /**待评价*/
 const int sEvaluate = 6;
 /**已完成*/
 const int sComplete = 8;



    /****************************************************** 用 户 登 陆 模 块 相 关 *************************************************************/

	 /** 用户基本信息数据模型 */
	 struct DriverBase{
	 	int oid; //用户码
	 	long uphone; //手机
	 	string urealname;//司机名字
	 	int code;//登陆成功失败返回码
        string msg;//登陆结果消息
	 };


   /****************************************************** 企 业 模 块 相 关 *************************************************************/

	 /** 企业与路线模型 */
	 struct DriverCompInfo{
	 			int compid;//企业码
        		string fname;//企业名称
        		string sname;//简称
        		int ctype;//企业性质
        		int csale;//企业规模
        		long contact;//联系方式
        		string phone;//电话
        		string address;//logo地址
        		string postcode;//邮编
	 };

	 /** 数组 司机相关所有企业信息 */
     sequence<DriverCompInfo> DriverCompInfoSeq;

/****************************************************** 订 单 数 据 模 块  *************************************************************/


    /** 订单数据简略模型 */
    struct OrderBrief{
        string adds;//地址   例如: 广州-深圳 或 深圳,福田 - 李维嘉 
        string time;//取货时间 或者 到货时间  例如: 11-11 13:00到11-15 16:00取货  或  11-25 8:00到11-26 16:00到货
        string cargo;//获取信息  例如:家具,9方,箱式货车,9.6米  或 无回单,回单原件
        string pay;//支付类型   例如: 线下支付  或  送货上门,送货不上楼
    };

    /** 订单数据详细模型  */
    struct OrderComplex{
        bool isValid;//有效性            如果订单赋值成功-设置对象有效性 true ,默认false
        string freightNo;//运单号        FF123456
        string sAdd;//出发地			 湖南省长沙市开福区五一大道亚达时代A栋1108
        string dAdd;//目的地			 湖南省株洲市芦淞区五一大道亚达时代A栋1108
        string cargoInfo;//货物信息      1吨,100件,木托,家具
        string carInfo;//车辆信息		 1台,9.6米,箱式货车,勿压
        string feeInfo;//费用信息		 无代收,无报价
        string pickTime;//取货时间		 11-11 13:00到11-15 16:00
        string contactInfo;//联系方式    15608447849,13873140557
        string takeInfo;//收货人相关信息 李世平,15608447849,送货不上楼,原件返回
        string arrivalTime;//到货时间    11-25 8:00到11-26 16:00
        string payType;//支付方式        线下支付 
        string payFee;//支付费用		 3000元
        string sendTime;//发单时间       2018-5-5 09:18:56
        string robTime;//抢单时间        2018-5-5 09:19:00
        string claimTime;//提货时间      2018-6-1 10:00:00
        string signTime;//签收时间		 2018-6-1 11:00:00
        string claimCode;//取货码
        bool isOnline;//是否 线上支付/线下支付
        bool isPay;//是否付款成功
        double fee;//自动计算的运费金额
    };

    /** 订单信息
     */
    struct OrderInfo{
        long orderNo;//订单号 - java服务端设置
        int state;//状态  - java服务端设置
        OrderBrief brief;//订单简单信息 - java服务端设置
        OrderComplex complex;//订单详细信息 - java服务端设置
    };

    sequence<OrderInfo> OrderInfoSeq;


	 /** 司机app服务 */
	 interface DriverService{

	    /**
	    司机登录
	    * phone: 司机登录电话号码
	    * pw:司机密码(MD5加密)
	    * 请判断角色码
	    */
         DriverBase driverLogin(string phone , string pw);
       
        /**
	    司机修改密码
	    * phone:司机手机号码
	    * opw:旧密码(MD5加密)
	    * npw:新密码(MD5加密)
	    */
         int driverChangePw(string phone ,string opw,string npw);

        /**
        根据 用户ID 查询其 所属全部企业列表信息
        *   userid 用户ID
        */
        idempotent DriverCompInfoSeq driverQueryCompList(int userid);

        /**
        根据 用户码 ,企业码 ,指定的订单状态 , 获取订单列表 - 简单信息
         *  userid 用户码
         *  enterpriseid 企业码
         *  state 查询的状态值
         *  return 订单对象数组 (包含订单简略信息)
         */
        idempotent OrderInfoSeq driverQueryOrderList(int userid, int enterpriseid, int state ,int year,int index,int range);

        /**
        查询单个订单的详情信息
        *   enterpriseid 企业号
        *   info 订单对象
        */
        idempotent OrderComplex driverQueryOrderInfo(int userid,int enterpriseid,long orderNo);

         /**
         查询单个订单的状态
         *   enterpriseid 企业号
         *   info 订单对象
         */
         idempotent int driverQueryOrderState(int enterpriseid,long orderNo);

         /**
         对订单进行状态修改操作
         *   userid 用户码 - 在取货时需要与用户码绑定
         *   enterpriseid 企业号
         *   info 订单号
         *   state 需要修改的状态
         *   操作成功返回true
         */
        bool driverOrderStateOp(int userid,int enterpriseid,long orderNo,int state);

        /**
        通知订单 修改金额
        *   enterpriseid 企业号
        *   info 订单对象
        *   操作成功返回true
        */
        bool driverSureOrderFee(int enterpriseid,long orderNo,double fee);

        /** 根据  用户码 ,企业编号,订单号,上传用户 原始行驶轨迹点 JSON 数据 0成功 */
        int driverUploadOriginal(int userid,int enterpriseid,long orderid,string json);

        /** 根据用户订单号 ,用户码 ,企业编号 ,上传用户 纠偏行驶轨迹点 JSON 数据 0成功 */
        int driverUploadCorrect(int userid,int enterpriseid,long orderid,string json);

        /** 根据用户订单号 ,用户码 ,企业编号 ,上传用户 走货节点信息 JSON 数据 0成功 */
        int driverUploadNode(int userid,int enterpriseid,long orderid,string node);

        /** 获取上传文件的文件夹路径 */
        string getUploadPath(string compid,string orderno);
     };
      	 
 };