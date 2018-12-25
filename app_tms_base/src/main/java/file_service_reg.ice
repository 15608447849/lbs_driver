[["java:package:bottle.distributed.register"]]
/** 文件服务分布式注册中心
    提供给客户端文件服务器的地址
*/
module reg{

    /** 文件服务器地址信息 */
    struct FSAddressInfo
    {
        bool isValid;
        string httpUrlPrev;
    };

    struct FSAddressConfig
    {
        string address; // 例如 192.168.1.155:8080
        int priority;//优先级
    };

    sequence<FSAddressConfig> FSAddressConfigList;

    /**
    提供 动态注册于删除文件服务器信息接口
    提供客户端 获取有效文件服务器信息
    */
    interface IFileServerCenter{

        /** 查询有效文件服务器地址 */
        FSAddressInfo queryFileServerAddress();
        /** 动态配置文件服务器地址信息 */
        bool dynamicRegistrationFileServerInfo(FSAddressConfigList list,bool isCovered);
        /** 动态删除文件服务器地址信息 */
        bool dynamicRemoveFileServerInfo(FSAddressConfigList list);
    };

};
