
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
</head>

<?php
$filename = "../data/info.xml";
#获取余额宝收益数据

require_once("../data/robot.php");

#比较是否是当天的数据
echo $today=date("Y-m-d");

if (strcmp($date, $today)){
	echo "数据未更新";
}
else
{		
	echo "相等,当天的数据";
	if(is_first($filename)){			
		#添加记录			
		$record = array();
		$record['date'] = $today;
		$record['code'] = '000198';
		$record['profit'] = $profit;
		$record['rate'] = $rate;
		add_record($filename,$record);
	}
}
/**
 * 判断是否是第一次发微博
 * @param string $url 请求地址
 * @param array $post_data post键值对数据
 * @return string
 */
function add_record($filename,$record) {
	echo '添加新的节点';
	$doc = new DOMDocument("1.0","utf-8");
	file_get_contents(  'saemc://'.'../data'.'/info.xml' , $doc);
	$root = $doc->documentElement;

	$info=$doc->createElement("info");  #创建节点对象实体
	$info=$root->appendChild($info);    #把节点添加到root节点的子节点

	$date=$doc->createAttribute("date");  #创建节点属性对象实体
	$date=$info->appendChild($date);  #把属性添加到节点info中
		
	$code=$doc->createElement("code");    #创建节点对象实体
	$code=$info->appendChild($code);
		
	$profit=$doc->createElement("profit");
	$profit=$info->appendChild($profit);
		
	$rate=$doc->createElement("rate");
	$rate=$info->appendChild($rate);
		
	$date->appendChild($doc->createTextNode($record['date']));  #createTextNode创建内容的子节点，然后把内容添加到节点中来
	$code->appendChild($doc->createTextNode($record['code']));
	$profit->appendChild($doc->createTextNode($record['profit'])); #注意要转码对于中文，因为XML默认为UTF-8格式
	$rate->appendChild($doc->createTextNode($record['rate']));
	
	file_put_contents(  'saemc://'.'../data'.'/info.xml', $doc->saveXML() );
}

/**
 * 判断是否是第一次发微博
 * @param string $url 请求地址
 * @param array $post_data post键值对数据
 * @return string
 */
function is_first($filename) {

	$doc = new DOMDocument("1.0","utf-8");
	file_get_contents(  'saemc://'.'../data'.'/info.xml',$doc);
	$infos = $doc->getElementsByTagName("info");
	$today = date ("Y-m-d");
	echo $today;
	$array  = array();
	foreach ($infos as $info){
		$date = $info->getAttribute("date");
		$array[] =$date;
	}
	if(in_array($today,$array))
	{
	    echo '当天记录已经更新';
		return 0;  
	}else
	{
	    echo '当天记录未更新';
		return 1;
	}
}

