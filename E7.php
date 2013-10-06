<?php
	/*
		The goal in this problem was simple: Find the 10001st prime number.  I just went with brute force solution for this one.
	*/

	set_time_limit(360);//To make sure that php doesn't stop the program from executing.
	nth_prime(10001);

	function nth_prime($n){
		$nth = 2;
		$current = 3;
		
		for($i = 1; $i < $n; $i ++){
			while(true){
				//echo "Checking " . $current . "<br>";
				if(is_prime($current)){
					$nth = $current;
					echo ($i + 1) . "th prime number: " . $current . "<br>";
					$current += 2;
					break;
				}
			$current += 2;
			}
		}
		echo "<h1>" . $n . "th prime: " . $nth . "</h1>";
	}
	
	//Return true if $n is prime and false otherwise
	function is_prime($n){
		for($i = 2; $i <= sqrt($n); $i++){
			if($n % $i == 0){
				return false;
			}
		}
		return true;
	}
?>