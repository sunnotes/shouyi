<?php

require_once 'weatherHelper.class.php';
require_once 'dbHelper.class.php';
require_once 'question.class.php';
require_once '../config.php';

class WeChat
{
	const TEXT_HELP = '请求帮助';
	const TEXT_EXCEPTION = '异常';
	const TEXT_WJ = '问题或建议';
	const TEXT_JS = '计算收益';
	const TEXT_CX = '查询收益';
	const TEXT_TQ = '天气查询';
	const TEXT_GL = '内部管理';
	const TEXT_OTHER = '其他';

	const ERROR_INFO = '哎呀，小宝发生异常了，请您再输入一次/:P-(';

	private $token;
	private $_msg;
	private $msgReceive;
	private $ado;
	
	private $weekarray=array('周日','周一','周二','周三','周四','周五','周六');

	public function __construct($token)
	{
		$this->token = $token;
		$this->ado = new DBHelper();
	}

	public function valid()
	{
		$echoStr = $_GET["echostr"];
		//valid signature , option
		if($this->checkSignature()){
			echo $echoStr;
			exit;
		}
	}

	/**
	 * For weixin server validation
	 */
	public function checkSignature()
	{
		$signature = $_GET["signature"];
		$timestamp = $_GET["timestamp"];
		$nonce = $_GET["nonce"];
		$token = $this->token;
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
	/**
	 * 获取微信服务器发来的信息
	 */
	public function getRevMsg()
	{
		//get post data, May be due to the different environments
		$postStr = $GLOBALS["HTTP_RAW_POST_DATA"];
		//extract post data
		if (!empty($postStr)){
		 return	$this->msgReceive =	$postObj = simplexml_load_string($postStr, 'SimpleXMLElement', LIBXML_NOCDATA);
		}else{
			return '';
		}
	}



	//receive Event
	public function receiveEvent($object)
	{
		switch ($object->Event)
		{
			case 'subscribe':
				$content = "感谢关注余额宝快报！我随时恭候在此，为您提供及时的资讯，同时提供收益实时查询！\n\n".
						"发送\"帮助\" 可以查看详细的使用指南。\n".
						"------\n".
						"关注余额宝快报，收益早知道！";
				break;
			case 'unsubscribe':
				$content = '感谢您关注余额宝快报！我们继续努力，为您提供更有用的信息！';
				break;
			default:
				$content = '欢迎关注「余额宝快报」'."\n\n".
						'我们将为您提供及时的余额宝资讯，同时提供余额宝收益实时查询'."\n\n".
						'发送 C 查询最新收益'."\n".
						'发送  金额(如 1000) 计算当前的收益'."\n".
						'发送 T 查看天气信息'."\n\n".
						'回复数字编码查看介绍：'."\n".
						'001  余额宝收益历史'."\n".
						'002  余额宝的收益是怎么计算的？'."\n".
						'003  余额宝为什么会出现日每万份收益下降而七日年化收益率却上升的现象？'."\n".
						'004  余额宝的收益是怎么计算的？'."\n".
						'005  余额宝的风险如何？会亏损吗？'."\n".
						'...... \n000  返回更多问题'."\n".
						'发送 H 更多信息等您发现。'."\n".
						'------'."\n\n".
						'关注余额宝快报，收益早知道！'."\n\n";
				break;
		}
		try {
			$user_id = $this->ado->query_user_id($object->FromUserName);
			$this->ado->add_msg_event($object, $user_id);
		} catch (Exception $e) {
		}
		return $content;
	}

	//receive Text
	public function receiveText($object)
	{
		//获取完整的用户信息
		$userinfo = $this->ado->query_user($object->FromUserName);
		if ($userinfo == 0) {
			return self::ERROR_INFO;
		}
		//return implode($userinfo, '#');
		//处理消息
		$request = trim($object->Content);
		$request = strtolower($request);
		//查询天气
		if ($request == 't'||$request == '天气' ) {
			if ($userinfo['city'] != '') {
				$type = self::TEXT_TQ;
				$weatherHelper = new WeatherHelper();
				$content = $weatherHelper->get_weatherinfo_from_city($userinfo['city']);
			}else {
				$type = self::TEXT_TQ;
				$content = '真是惭愧，我还不知道您在哪儿呢。告诉我您所在的位置，我就能告诉您天气信息了哦/:,@-D'
							."\n\n"
							.'发送位置的方法：'
							."\n"
							.'聊天界面下点击左下角的 + 号，选择 位置，点击 发送 按钮即可'
							."\n\n"
							."提示：只需要发送一次，以后就可以通过输入 T 查询天气咯";
			}
		}elseif ($request == 'c'||$request == '余额宝' || $request == '查询' || $request == '收益'){
			//查询收益
			$type = self::TEXT_CX;
			$record = $this->ado->getlast();
			$record_date = strtotime($record['date']);
			$content = "最新收益播报:\n".
					date('n月j号',$record_date).' ('.$this->weekarray[date('w',$record_date)].")\n".						
					'万份收益: '.$record['profit']."元\n".
					$record['tendency'].
					"\n七日年化收益: ".$record['rate']."%\n";
		}elseif ($request == 'h'||$request == '帮助'||$request == '?'||$request == '？'){
			//帮助
			$type = self::TEXT_HELP;
			$content = "您好，我是小宝/:pig：\n\n".
					"发送 C 查询最新收益\n".
					"发送  金额(如 1000) 计算当前的收益\n".
					"发送 T 查看天气信息\n\n".
					"回复数字编码查看介绍：\n".
					"001  余额宝收益历史\n".
					"002  余额宝的收益是怎么计算的？\n".
					"003  余额宝为什么会出现日每万份收益下降而七日年化收益率却上升的现象？\n".
					"004  余额宝的收益是怎么计算的？\n".
					"005  余额宝的风险如何？会亏损吗？\n".
					"...... \n000  返回更多问题\n\n".
					"发送 J+建议(如J祝余额宝快报越来越好!) 向小宝提建议\n".
					"发送 W+问题(如W余额宝收益怎么计算的?) 向小宝提问\n".
					"小宝会在第一时间回复的哦/:hug\n\n".
					"------\n".
					"关注余额宝快报，收益早知道！\n";
		}elseif (preg_match('/^([1-9][0-9]*(\.[0-9]{1,2})?)$/', $request,$res)){
			$type = self::TEXT_JS;
			$money = $request;
			$record = $this->ado->getlast();
			$record_date = strtotime($record['date']);
			$earn = $money * $record['profit'] /10000 ;
			$earn = number_format($earn,2);
			$content = '您输入的金额为'.$money. '元，万份收益为'.
					$record['profit'].'元。'."\n\n".
					'恭喜您，您'.date('j',$record_date+86400).'号('.$this->weekarray[date('w',$record_date+86400)].
					')能收益 '.$earn.'元 !/:handclap '.
					"\n\n------\n".
					'收益会在'.						
					date('j',$record_date+86400).
					'号 15:00 前发放到余额宝帐户。';
		}elseif (preg_match('/^[\d]{3}$/', $request)){
			$qa = new Question();
			if (trim($request) == '000') {
				$type = self::TEXT_WJ;
				$content = $qa->getAllQuestionList();
			}elseif (trim($request) == '001'){
				$type = self::TEXT_CX;
				$content = $this->ado->getHistory();
			}else {
				$type = self::TEXT_WJ;
				$content = $qa->getAnswer(trim($request));
			}
		}elseif (preg_match('/^(j)(.+)$/', $request,$res)){
			$type = self::TEXT_WJ;
			$question = $res[2];
			$content ="您提交的建议是: $question /:share\n小宝会尽快与您联系,感谢您对小宝的关注。/:,@-D";
		}elseif (preg_match('/^(l)(.+)$/', $request,$res)){
			$type = self::TEXT_GL;
		}elseif (preg_match('/^(w)(.+)$/', $request,$res)){
			$type = self::TEXT_WJ;
			$question = $res[2];
			$content ="您提交的问题是: $question /:?\n\n";
			$qa = new Question();
			$reply = $qa->getQuestionList($question);		
			if ($reply == false) {
				$content =$content."您提交的问题，小宝还不知道怎么回答。/:P-( 小宝会尽快与您联系,感谢您对小宝的关注。/:,@-D";;
			}else{
				$content = $content.$reply;
			}
		}elseif ($request == 'eason'){
			$type = self::TEXT_GL;
			$record = $this->ado->getlast();
			$record_date = strtotime($record['date']);
			$content = "最新收益播报:\n".
					date('n月j号',$record_date).'('.$this->weekarray[date('w',$record_date)].")\n".			
					'万份收益: '.$record['profit']."元\n".
					$record['tendency'].
					"\n七日年化收益: ".$record['rate']."%\n\n收益会在".			
					date('j',$record_date+86400).'号 ('.
					$this->weekarray[date('w',$record_date+86400)].') 15:00 前发放到余额宝账户。'.
					"\n------\n微信关注[余额宝快报]，收益早知道！";
		}else {
			$qa = new Question();
			$content = $qa->getQuestionList($request);
			if ($content == false) {
				$type = self::TEXT_OTHER;
				$content = "亲，您刚刚发送的信息小宝还无法识别/::< \n发送 帮助 或者 H ，可以查看详细的使用指南。/:,@-D";
			}else {
				$type = self::TEXT_WJ;
			}
		}
		$this->ado->add_msg_text($object, $userinfo,$type);
		return $content;
	}
	//receive Location
	public function receiveLocation($object)
	{
		$user_id = $this->ado->query_user_id($object->FromUserName);
		if ($user_id == 0) {
			return self::ERROR_INFO;
		}
		$this->ado->add_msg_location($object, $user_id);
		//读取请求
		$weatherHelper = new WeatherHelper();
		$city = $weatherHelper->get_cityinfo_from_location($object->Location_X, $object->Location_Y);
		if($city != ''){
			$content = $weatherHelper->get_weatherinfo_from_city($city);
			$this->ado->update_city_info($object->FromUserName,$city);
		}else {
			return self::ERROR_INFO;
		}			
		return $content;
	}
	//response Text
	public function responseText($object, $content, $flag = 0)
	{
		$textResp = "<xml>
				<ToUserName><![CDATA[%s]]></ToUserName>
				<FromUserName><![CDATA[%s]]></FromUserName>
				<CreateTime>%s</CreateTime>
				<MsgType><![CDATA[%s]]></MsgType>
				<Content><![CDATA[%s]]></Content>
				<FuncFlag>0</FuncFlag>
				</xml>";
		$resultStr = sprintf($textResp, $object->FromUserName,$object->ToUserName,time(),'text', $content);
		return $resultStr;
	}

}