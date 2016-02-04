<?php

/*
 * https://projecteuler.net/problem=61
 *
 * For this problem I needed to find a set of six four-digit cyclic numbers, such that each of six sets was represented by one of these numbers.
 *
 * The sets were the triangle, square, pentagonal, hexagonal, heptagonal and octagonal sets. Each one has a different formula.
 *
 * When I looked at this problem, what I essentially saw was a tree. The first level of this tree is the set that I started off with. The children of each node in this tree
 * were all numbers in every other set. Each node in the tree has numbers from all "remaining" sets, in other words all sets that the node is not in, and all sets
 * that are not a parent of that node.
 *
 * My method is a depth first search method. It starts off with the triangular set (though any set could be used) then examines all children in that set. Each
 * time it finds one that is in sequence, it calls the same method on that node. Nodes that are in sequence are ones where the first one's last two digits are equal
 * to the first two digits of the second. For example, 1082 and 8256 are in sequence.
 */

//All of the sets were stored in the same array to make them easier to access. This avoids the problem of creating a separate function for each set
$sets = array(
	'octagonal' => array(),
	'heptagonal' => array(),
	'hexagonal' => array(),
	'pentagonal' => array(),
	'square' => array(),
	'triangle' => array(),
);


//The following while loops populate all of the sets. The functions triangle, square etc are implemented below
$n = 1;

while(true) {
	$new_number = triangle($n);
	
	//The division by 10000 is to ensure that we stop once a 5 digit number is found
	if(floor($new_number / 10000) > 0) {
		break;
	}
	else if(floor($new_number / 1000) > 0){//The division by 1000 was a way to make sure that no numbers under 4 digits were added. Crude, but it works
		$sets['triangle'][] = $new_number;
	}
	
	$n ++;
}

$n = 1;

while(true) {
	$new_number = square($n);
	
	if(floor($new_number / 10000) > 0) {
		break;
	}
	else if(floor($new_number / 1000) > 0){
		$sets['square'][] = $new_number;
	}
	
	$n ++;
}

$n = 1;

while(true) {
	$new_number = pentagonal($n);
	
	if(floor($new_number / 10000) > 0) {
		break;
	}
	else if(floor($new_number / 1000) > 0){
		$sets['pentagonal'][] = $new_number;
	}
	
	$n ++;
}

$n = 1;

while(true) {
	$new_number = hexagonal($n);
	
	if(floor($new_number / 10000) > 0) {
		break;
	}
	else if(floor($new_number / 1000) > 0){
		$sets['hexagonal'][] = $new_number;
	}
	
	$n ++;
}

$n = 1;

while(true) {
	$new_number = heptagonal($n);
	
	if(floor($new_number / 10000) > 0) {
		break;
	}
	else if(floor($new_number / 1000) > 0){
		$sets['heptagonal'][] = $new_number;
	}
	
	$n ++;
}

$n = 1;

while(true) {
	$new_number = octagonal($n);
	
	if(floor($new_number / 10000) > 0) {
		break;
	}
	else if(floor($new_number / 1000) > 0){
		$sets['octagonal'][] = $new_number;
	}
	
	$n ++;
}

$sequence = array();

//All sets that have yet to be checked. One of these sets is removed with each recursive call, progressing towards the base case
//of having no sets left to remove
$remaining_sets = array(
	'octagonal' => 'octagonal',
	'heptagonal' => 'heptagonal',
	'hexagonal' => 'hexagonal',
	'pentagonal' => 'pentagonal',
	'square' => 'square',
	'triangle' => 'triangle',
);

$the_set = compare(array(), 'triangle', $remaining_sets);

//Recursively traverse a tree of numbers to find a sequence that fits the description of the problem
function compare($sequence = array(), $current_set, $remaining_sets) {
	global $sets;
	
	//Base case
	if(empty($remaining_sets)) {//If all sets have been checked
		reset($sequence);
		$first = current($sequence);//Store the first number in the sequence
		if(in_sequence(end($sequence), $first)) {//If the first number is in sequence with the last number
			echo "The set is...<br/>";
			echo var_dump($sequence) . "<br/>";
			
			$sum = 0;
			
			//Print out that sequence
			foreach($sequence as $current) {
				$sum += $current;
			}
			echo "And the sum is: " . $sum . "<br/>";//and print out the sum
		}
	} else {
		unset($remaining_sets[$current_set]);//Remove the current set from the list of remaining sets
		foreach($sets[$current_set] as $current) {//Loop through all numbers in the current set
			unset($sequence[$current_set]);//The last one was a dud, so remove it (if we don't do this we'll compare the next one to it. Bleh)
			if(in_sequence(end($sequence), $current) || empty($sequence)) {//If the number is in sequence with the current one, proceed
				$sequence[$current_set] = $current;//Add it to the sequence
				foreach($remaining_sets as $set) {//Loop through the remaining sets and do the comparisons
					compare($sequence, $set, $remaining_sets);
				}
				//If there are five numbers in the sequence, this 'if' is necessary. Otherwise the recursive call will be skipped
				//and the base case will never be reached
				if(empty($remaining_sets)) {
					compare($sequence, '', array());
				}
			}
		}
	}
}


/**
	Check if two numbers $n1 and $n2 are in sequence. That is whether the two leftmost digits of $n1 are the two right most digits of $n2
*/
function in_sequence($n1, $n2, $print = false) {
	$n2_digits = floor($n2 / 100);//Get the first two digits of n2
	$n1_digits = $n1 % 100;//Get thelast two digits of n1
	
	if($n1_digits == $n2_digits) {
		return true;
	}
	else {
		return false;
	}
}

/*
 * All functions to calculate each number in each set
 */

function triangle($n) {
	return ($n * ($n + 1)/2);
} 

function square($n) {
	return $n * $n;
}

function pentagonal($n) {
	return ($n * ((3 * $n) - 1)/2);
}

function hexagonal($n) {
	return $n * (2 * $n - 1);
}

function heptagonal($n) {
	return ($n * (5* $n - 3)/2);
}

function octagonal($n) {
	return $n * (3 * $n - 2);
}