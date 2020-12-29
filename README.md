# Onex-Coder
![img](https://raw.githubusercontent.com/zhangchaoxu/onex-coder/master/src/main/resources/static/images/logo.png)

## 描述
代码生成器，通过表生成接口代码，VUE前端代码，并且支持生成数据库文档。

## 在线服务
[Onex-Coder](https://onex-coder.nb6868.com)

## *注意*
需要确保数据库开放了外网访问,并且运行程序所在服务器与数据库服务器网络通畅。      
程序提供web支持，服务器端不存储数据库连接的信息，若担心安全问题，请下载源程序在本地运行。

## linux部署脚本
```shell
process=`ps -fe|grep "onex-coder.jar" |grep -ivE "grep|cron" |awk '{print $2}'`
if [ !$process ];
then
	echo "stop onex-coder.jar process $process ....."
	kill -9 $process
	sleep 1
fi

echo "start onex-coder.jar process....."
nohup java -jar onex-coder.jar --server.servlet.context-path=/ 2>&1 | cronolog log.%Y-%m-%d.out >> /dev/null &
echo "start onex-coder.jar success!"
```

## Thanks
1. 表结构文档生成功能由[SCREW](https://github.com/pingfangushi/screw)提供技术支持
2. [ICON](https://www.iconfinder.com/icons/6569384/and_application_applications_code_coding_programming_web_icon)
