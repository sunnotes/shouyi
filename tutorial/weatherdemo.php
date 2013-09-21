<?php
$baidu_map_api_url = "http://api.map.baidu.com/telematics/v3/weather?location=北京&output=json&ak=BEdc956eb36d4518c6bf8257763abbd6";

//调用SAE的类
$saeFetchurl = new SaeFetchurl();
//取百度的地址解析结果
$baidu_return = $saeFetchurl->fetch($baidu_map_api_url);
if($saeFetchurl->errno == 0){
	//匹配成功
	$object = json_decode($baidu_return,true);
	if(trim($object['status'])=='success'){
		echo $object;
		echo print_r($object);
// 		$city =  $object['result']['addressComponent']['city'];
// 		$city = str_replace(array('市','县','区'), array('','',''), $city);
	}
}
return $city;