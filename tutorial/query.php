<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
</head>


<?php

include('dbhelper.php');
include('robot.php');
include 'weibodemo.php';
$ado = new ADO();
$record = $ado->getlast();
echo $date = $record[date];

echo $today=date("Y-m-d");
//$today=date("2013-07-18");
/*
 * strcmp(string1,string2)
* 该函数返回：
* 0 - 如果两个字符串相等
* <0 - 如果 string1 小于 string2
* >0 - 如果 string1 大于 string2
*/
// if (!strcmp($date, $today)){
// 	echo "数据库已是最新的数据，不需要更新";
// }
// else
// {
	echo "数据库不是最新的数据，需要更新";
	$robot = new Robot();
	$record = $robot->getlastRecord();
	//存数据库
	//$ado->insert($record[date], $record[profit], $record[rate]);
	//发微博
	$weibo = new Weibo();
	$weibo->send($record[date], $record[profit], $record[rate]);
	print_r($record);
//}
