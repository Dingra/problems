<?php

	/*
		The objective of this problem was to find there numbers a, b and c with a < b < c and a^2 + b^2 = c^2 such that a + b + c = 1000 and report their product.  The only real challenge here was that PHP's function to check to see if an number is an integer does not check the number itself, but the type of variable that it is.  For some reason, I couldn't get my integer checker to work with the modulo operator, but I eventually realised that all I had to do was compare the number to its floor.
		
		This solution actually prints out two solutions with a + b + c = 1000, even though there is only one that satisfies the property, but since it can be seen plainly when a > b, I didn't think it was a problem.
	*/
	
	for($c = 1000; $c >= 2; $c --){
		for($b = $c - 1; $b >= 1; $b --){
			$a = sqrt(pow($c, 2) - pow($b, 2));
			
			if(int_test($a) && ($a + $b + $c == 1000))
				echo "a: " . $a . " b: " . $b . " c: " . $c . "<br>" . "a * b * c = " . ($a * $b * $c) . "<br>";
		}
	}
	
	//Return true if an input $n is an integer (it does not have any digits right of the decimal) and false otherwise
	function int_test($n){
		if($n == floor($n))
			return true;
		else return false;
	}
	
?>