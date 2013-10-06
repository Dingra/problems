<?php
	set_time_limit(6000);

	$sum = 2;//2 is a prime number
	for($i = 3; $i < 2000000; $i += 2){
		if(is_prime($i)){
			$sum += $i;
			echo $i . "<br>";
		}
	}
	
	echo "<h1>The sum is:</h1>" . $sum;
	
	//Return true if $n is prime and false otherwise
	function is_prime($n){
		for($i = 2; $i <= sqrt($n); $i++){
			if(is_factor($i, $n)){
				return false;
			}
		}
		return true;
	}
	
	//Return true if $m is a factor of $n and false otherwise.
	function is_factor($m, $n){
		if(($n % $m) == 0)
			return true;
		else return false;
	}
?>