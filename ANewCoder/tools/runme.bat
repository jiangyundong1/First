color 30
echo off
echo 本脚本依赖jdk1.6或以上版本（xjc.exe bin目录），以及xsd.exe（本目录）,确认后请继续
pause
echo 由sample.xml生成sample.xsd
xsd sample.xml
echo 请修改sample.xsd，细化设计xsd,确认后请继续
pause
echo 由sample.xsd，生成对应的jaxb类
xjc sample.xsd
echo 现在，你可以用jaxb类的方法读入符合xsd验证的xml信息到生成的jaxb类中，并通过javaapi访问相应的信息了
echo 确认后退出
pause