<?php
class WeatherHelper{
	public function get_cityinfo_from_location($location_x,$location_y){
		$city = '';
		//准备回复数据
		//使用百度API
		$baidu_map_api_url = 'http://api.map.baidu.com/geocoder?output=json&';
		$baidu_map_key = 'ebad1e950d1f95a2656ffe633662659f';
		//调用SAE的类
		$saeFetchurl = new SaeFetchurl();
		//取百度的地址解析结果
		$baidu_return = $saeFetchurl->fetch($baidu_map_api_url.'location='.
				$location_x.','.$location_y.'&key='.$baidu_map_key);
		if($saeFetchurl->errno == 0){
			//匹配成功
			$object = json_decode($baidu_return,true);
			if(trim($object['status'])=='OK'){
				$city =  $object['result']['addressComponent']['city'];
				$city = str_replace(array('市','县','区'), array('','',''), $city);
			}
		}
		return $city;
	}


	public function get_weatherinfo_from_city($city){

		$content ="天气信息播报：\n\n";
		if($city == ''){
			return $content.'无效城市信息';
		}
		$baidu_weather_api = 'http://api.map.baidu.com/telematics/v3/weather?output=xml&ak=BEdc956eb36d4518c6bf8257763abbd6&location='
				.$city;
		$xml = simplexml_load_file($baidu_weather_api);
		if( $xml == ''|| $xml->children()->status !='success')
		{
			return $content.'抱歉，系统异常...';
		}
		//读取成功
		$content = $content.$xml->children()->results->currentCity . "\n";
		//
		$weather_data = $xml->children()->results->weather_data;
		$k =1;
		foreach($weather_data->children() as $child)
		{
			if ($child->getName()=='date'){
				$content = $content.$child . "\n";
			}elseif ($child->getName() == 'weather'){
				$content = $content.'气候：'.$child . "\n";
			}elseif ($child->getName() == 'wind'){
				$content = $content.'风级：'.$child . "\n";
			}elseif ($child->getName() == 'temperature'){
				$content = $content.'温度：'.$child . "\n";
			}
			if($k++ % 6 === 0)
				$content = $content.  "\n";
		}
		return $content;
	}
}