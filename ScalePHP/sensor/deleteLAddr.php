<?php
/**
 * 删除某一lAddr及其对应传感器的所有数据
 */
include "../lib/DBLink.php";//里面有一个到数据库的连接$db
include "../lib/Errors.php";//里面有错误常量
include "../lib/ObjToXML.php";//里面有错误常量
include "../lib/Sensor.php";
$result=Sensor::deleteLAddr($_POST["lAddrID"]);
buildXML($result);
?>