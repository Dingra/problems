<?php

	/*
		The objective for this problem was to find the triangular first triangular number with at least 500 factors.  A triangular number is one that is 1 + 2 + 3 + ... + n + (n + 1).  This was very easy to do by brute force but it took a long time to run.
	*/

	set_time_limit(6000);

	$i = 1;
	$sum = $i;
	
	while(true){
		//$n = triangular($i);
		$arr = factor($sum);
		if(sizeof($arr) > 500){
			echo $sum;
			break;
		}
		$i ++;
		$sum += $i;
	}
	
	//Return an array of factors of a number $n
	function factor($n){
		$ret = array();
		
		for($i = 1; $i <= sqrt($n); $i ++){
			if(($n % $i) == 0){
				array_push($ret, $i);
				if($i != sqrt($n))
					array_push($ret, $n/$i);
			}
		}
		
		return $ret;
	}
?>