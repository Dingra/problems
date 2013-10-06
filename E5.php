<?php

	/*
		The challenge here was to find the lowest common multiple of all numbers from 1 to 20.  Of course I could have found the answer using a simple, brute force solution, but that would take the challenge out of it.  To find a more efficient solution, I had to do a bit of research.
		
		I went to Youtube and found this video describing prime factorization trees: http://www.youtube.com/watch?v=YbuFd_jio28
		
		Basically, the strategy was to find the prime factorization of each number I was trying to find the lowest common multiple for.  The prime factorization is a list of all prime numbers which are factors of the number being checked.  For example, the prime factorization for 4 is (2, 2).  The prime factorization for 20 is (2, 2, 5).
		
		After the prime factorization is found, every distinct prime factor is raised to the power of the number of times it occurs.  For example, if the number being checked is 4, the factor 2 which occurs twice is given an exponent of 2.  If the number being checked is 8, the factor 2 is given an exponent of 3.
		
		Once this is done, one find the highest exponent for each prime factor for all numbers being checked.  For example, the prime factor 2 of for has an exponent of 2 because it occurs twice, 3 for 8 because it occurs 3 times and 4 for 16 because it occurs 4 times (2 * 2 * 2 * 2) = 16.
		
		Finally, one must raise each prime factor to its exponent and multiply them all together to get the lowest common multiple of all numbers being checked.
	*/
	
	lowest_common_multiple(20);
	
	function lowest_common_multiple($n){
		$big_bucket = get_big_bucket($n);
		$final_bucket = array();
		$max = 0;//The highest power of a factor.
		
		for($i = 0; $i < 100; $i ++){
			$max = 0;
			foreach($big_bucket as $b){
				if($b[$i] > $max)
					$max = $b[$i];
			}
			$final_bucket[$i] = $max;
		}
		
		$total = 1;
		
		for($i = 2; $i < 100; $i ++){
			$total *= pow($i, $final_bucket[$i]);
		}
		
		echo "<h1>TOTAL IS..." . $total . "</h1>";
	}
	
	//Get an array with an index for each digit integer from 2 to $n.  Each index in an array represents a prime factor.
	//The number in the array at that index is the number of times that number appears as a prime factor.
	function get_big_bucket($n){
		$big_bucket = array();
		for($i = 2; $i <= $n; $i ++){
			$r = reduce($i);
			
			$b = bucket($r);
			$big_bucket[$i] = $b;
		}
		return $big_bucket;
	}
	
	//Place each item in an array $arr into a 'bucket' using the value as an index of the bucket and the
	//number of times it occurs in $arr as the value in the bucket
	function bucket($arr){
		$bucket = array();
		
		
		for($i = 0; $i < 100; $i ++)//Assuming no prime numbers over 100 will be needed
			array_push($bucket, 0);		
			
		foreach($arr as $i){
			if($bucket[$i] > 0)
				continue;
			foreach($arr as $j){
				if($j == $i)
					$bucket[$i] ++;
			}
		}
		
		return $bucket;
	}
	
	//Reduce $n to an array of its prime factors
	function reduce($n){
		//echo "The method is being called for " . $n . "<br>";
		$prime_factors = array();
		
		if(is_prime($n)){//Only occurs when the method is initally called with a prime number for the input
			array_push($prime_factors, $n);
			return $prime_factors;
		}
		/*
			Must first search for a factor of $n.  Once one is found, another factor can be find by dividing $n by that factor.  There are four possible cases...
			1: Both numbers are prime.  Since we are looking for prime factors, they must both be added to the array
			2: The first number is prime and the second one is not.  The first number must then be added to the array of factors, and the second number must be broken down to find its prime factorization.  The reduce method is called again on this number
			3: Same as case 2 but with the first number being composite and the second being prime
			4: Neither one is prime, so the prime factorization must be found for both of them.  Call the reduce method on both.
			
			Since the method must eventually return two numbers, the easiest way to do this is with an array, which is merged with the final array
			when it is called.
		*/
		else{
			for($i = 2; $i <= sqrt($n); $i ++){//Search for a factor
				if(($n % $i) == 0){				
					if(is_prime($i) && is_prime($n / $i)){//base case: when both factors are prime
						$temp = $n / $i;
						array_push($prime_factors, $temp);
						array_push($prime_factors, $i);//just add them to the array
					}
						
					else if(is_prime($i) && !is_prime($n / $i)){//When one is prime but the other is not, must reduce the other one
						$primed = reduce($n / $i);						
						array_push($prime_factors, $i);
						$prime_factors = array_merge($prime_factors, $primed);
					}
					
					else if(!is_prime($i) && is_prime($n / $i)){
						$primed = reduce($i);
						array_push($prime_factors, ($n / $i));
						$prime_factors = array_merge($prime_factors, $primed);
					}
						
					else{//Neither one is prime
						$primed_1 = reduce($i);
						$primed_2 = reduce($n / $i);
						
						$prime_factors = array_merge($prime_factors, $primed_1, $primed_2);
					}
						
					break;//Once one factor is found, there is no need to find more
				}
			}
		}
		return $prime_factors;
	}
	
	
	//Return true if $m is a factor of $n and false otherwise.
	function is_factor($m, $n){
		if(($n % $m) == 0)
			return true;
		else return false;
	}
	
	//Return true if $n is prime and false otherwise
	function is_prime($n){
		for($i = 2; $i <= sqrt($n); $i++){
			if(is_factor($i, $n)){
				return false;
			}
		}
		return true;
	}
?>