<?php
/**
 * wechat php test
 */
require_once 'weChat.class.php';
//define your token
define("TOKEN", 'eason');
$weChatAPI = new WeChatAPI();
$weChatAPI->valid();
$weChatAPI->responseMsg();

class WeChatAPI
{
	public function valid()
	{
		$echoStr = $_GET["echostr"];
		//valid signature , option
		if($this->checkSignature()){
			echo $echoStr;
		}
	}

	public function responseMsg()
	{
		$wechat = new WeChat(TOKEN);
		//get post data, May be due to the different environments
		$postStr = $GLOBALS["HTTP_RAW_POST_DATA"];
		//extract post data
		if (!empty($postStr)){
			$postObj = simplexml_load_string($postStr, 'SimpleXMLElement', LIBXML_NOCDATA);
			$msgType = $postObj->MsgType;
			switch ( trim( $msgType ) ){
				//文本消息
				case 'text':
					{
						$contentResp = $wechat->receiveText($postObj);
					}
					break;
					//地理位置消息
				case 'location':
					{
						$contentResp = $wechat->receiveLocation($postObj);
					}
					break;				
					//事件消息
				case 'event':
					{
						$contentResp = $wechat->receiveEvent($postObj);
					}
					break;
					//图片消息
				case 'image':					
					//链接消息
				case 'link':					
				default:
					{
						$contentResp = "哎呀,您发送的信息小宝暂时还无法识别咧...蹲墙角反省去了/:P-(  ";
					}
					break;
			}
			echo $wechat->responseText($postObj, $contentResp);
		}else {
			echo "system error";
			exit;
		}
	}

	private function checkSignature()
	{
		$signature = $_GET["signature"];
		$timestamp = $_GET["timestamp"];
		$nonce = $_GET["nonce"];

		$token = TOKEN;
		$tmpArr = array($token, $timestamp, $nonce);
		sort($tmpArr);
		$tmpStr = implode( $tmpArr );
		$tmpStr = sha1( $tmpStr );

		if( $tmpStr == $signature ){
			return true;
		}else{
			return false;
		}
	}
}

?>