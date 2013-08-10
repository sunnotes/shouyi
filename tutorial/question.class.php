<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
</head>

<?php
class Question
{
	/*
	* $question  用户提交的问题
	*/
	public function getQuestionList($question)
	{
		//预定义的关键词
		static $keywordlist = array(
				'1' => '收益',
				'2' => '结算',
				'3' => '万份',
				'4' => '年化',
				'5' => '7日',
				'6' => '七日',
				'7' => '计算',
				'8' => '风险',
				'9' => '亏损',
				'10' => '投资额',
				'11' => '交易',
				'12' => '费用',
				'13' => '转出',
				'14' => '资金',
				'15' => '存款',
				'16' => '利息',
				'17' => '开始',
				'18' => '转入'
		);
		//抽取问题中的关键词
		$i = 0;
		foreach ($keywordlist as $keyword) {
			if(strpos($question, $keyword)!==false)
			{
				$keywords["$i"] = $keyword;
				$i++;
			}
		}
		//通过LIKE查询，获取和关键词关联的问题
		$sql = 'SELECT  code ,question  FROM question WHERE 1!=1 ' ;
		if (isset($keywords)) {
			foreach ($keywords as $keyword){
				$sql = $sql." OR question LIKE '%$keyword%' ";
			}
			$sql = $sql." order by id";
		}else {
			return false;
		}
		$mysql = new SaeMysql();
		$result = $mysql->getData( $sql );
		$content ="小宝猜您想知道:\n";
		foreach ($result as $record){
			$content = $content.$record['code'].' '.$record['question']."\n";
		}
		$content = $content."......\n 回复数字编码查看回答 \n 000  返回更多问题";
		return $content;
	}

	/*
	* 返回所有的问题单
	*/
	public function getAllQuestionList()
	{		
		$sql = 'SELECT  code ,question  FROM question order by id ' ;		
		$mysql = new SaeMysql();
		$result = $mysql->getData( $sql );
		$content ="回复数字编码查看回答:\n";
		foreach ($result as $record){
			$content = $content.$record['code'].' '.$record['question']."\n";
		}
		$content = $content."......\n 000  返回更多问题";
		return $content;
	}
	
	/*
	* $questioncode  问题编码
	* 返回问题答案，其中 000 0001 需特殊处理
	*/
	public function getAnswer($questioncode) {
		$sql = "SELECT  question,answer  FROM question WHERE code = $questioncode " ;
		$mysql = new SaeMysql();
		$result = $mysql->getData( $sql );
		if ($result == '') {
			$content = "你输入的数字编码无效...\n 000  返回更多问题";
		}else {
			$content = $questioncode.' '.$result[0]['question']."\n\n".$result[0]['answer']."\n"."......\n 000  返回更多问题";
		}
		return $content;
	}	
}