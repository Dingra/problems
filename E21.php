<?php
	/*
		The purpose of this problem was to find the sum of all amicable numbers under 10000.  A number is amicable if the sum of all of its factors aside from itself sum up to another number whose factors (again aside from itself) sum up to the original number.  For example, 220 and 284 are amicable because the sum of the factors of 220 (not including 220) equals 284, and the sum of the factors of 284 equal 220.  A number cannot be an amicable pair with itself.
	*/

	$sum = 0;
	
	//Check all numbers under 10000 to see if they are amicable.  If they are, add them to the sum.
	for($i = 2; $i < 10000; $i ++){
		if(amicable($i)){
			echo "<br>" . $i . " and " . d($i);
			$sum += $i;
		}
	}
	
	echo "<BR>" . $sum;

	//Get the sum of all factors of $n, not including $n
	function d($n){
		$sum = 0;
		for($i = 2; $i < sqrt($n); $i ++){
			if($n % $i == 0){
				$sum += $i;
				if($i != sqrt($n));//Make sure that a number is not counted twice if it is a square root
					$sum += $n / $i;
			}
		}
		$sum ++;
		
		return $sum;
	}
	
	//Check to see if two numbers are amicable
	function amicable($n){
		$sum1 = d($n);
		$sum2 = d($sum1);
		
		if($sum2 == $n && $sum1 != $n)
			return true;
		else return false;
	}
	
?>