
<?php

class Weibo
{	
	public function send($date,$profit,$rate)
	{
		#发微博
		//2.002ah_TDjNEc2B8aec74d914ELKBvB kuaibao
		//2.00wJm9pBMRLwZEa34f8a3ea3kJTKoD  ieason8			
		$formattoday = date ("Y/m/d");
		$post_data = array(
				'access_token' => '2.002ah_TDjNEc2B8aec74d914ELKBvB',
				'status' => '#余额宝快报# '.$formattoday.' 万份收益'.$profit.' , 七日年化收益率'.$rate.'%[威武]  查询每天收益额 , 请关注余额宝快报微信 , 微信号: ali_bao '
		);
		$res = $this->send_post('https://api.weibo.com/2/statuses/update.json', $post_data);
	}
	
	/**
	 * 发送post请求
	 * @param string $url 请求地址
	 * @param array $post_data post键值对数据
	 * @return string
	 */
	private function send_post($url, $post_data) {
		$postdata = http_build_query($post_data);
		$options = array(
				'http' => array(
						'method' => 'POST',
						'header' => 'Content-type:application/x-www-form-urlencoded',
						'content' => $postdata,
						'timeout' => 15 * 60 // 超时时间（单位:s）
				)
		);
		$context = stream_context_create($options);
		//echo $context;
		$result = file_get_contents($url, false, $context);
		return $result;
	}
	
}