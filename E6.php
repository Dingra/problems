<?php
	/*
		Here I had to find the difference between the square of the sums (1 + 2 + 3 +...+ n)^2 and the sum of the squares (1^2 + 2^2 + 3^2 +...+n^2) of the first 100 natural numbers.  Cake.
	*/

	echo difference(100);

	function sum_of_squares($n){
		$sum = 0;
		for($i = 1; $i <= $n; $i ++){
			$sum += pow($i, 2);
		}
		
		return $sum;
	}
	
	function square_of_sums($n){
		$sum = 0;
		for($i = 1; $i <= $n; $i ++){
			$sum += $i;
		}
		return pow($sum, 2);
	}
	
	function difference($n){
		return square_of_sums($n) - sum_of_squares($n);
	}
?>