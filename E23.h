/*
	Problem 23.  An abundant number is a number that is less than the sum of its proper divisors.  A proper divisor of a number is a factor of that number
	that is not equal to that number.  For example, the proper divisors of 12 are 1, 2, 3, 4 and 6 (not 12).  The sum of the proper divisors of 12 is 16, so
	12 is an abundant number(and in fact, it is the smallest).
	
	I am given that there are no integers greater than 28123 that cannot be written as the sum of two abundant numbers, but also that the actual upper limit
	is lower.  My task was to find and report the sum of all numbers that could not be written as the sum of two abundant numbers.
	
	My strategy was to first find all abundant numbers from 12 to 28123.  To do this, I needed to factor them, and add up the proper divisors.  I stored the
	abundant numbers in a vector.  Once this was done, I looped through the vector of abundant sums, and added each to together.  I kept track of these sums
	in an array.  Any number found in these two for loops was given a value of 1 in the array, the rest had the default value of zero.
	
	Once I had this array of 1s and 0s, I looped through it, adding all the indices with a value of 0 to a sum variable, then reported that sum.
*/

#include "stdafx.h"
#include <vector>
#include <math.h>
#include <iostream>


using namespace std;

bool isAbundant(int n);								//Return 1 if a number is abundant, 0 otherwise
vector <int> factor(int n);							//Find all proper divisors of n (all factors not including n)
vector <int> findAbundantNumbers();					//Find all abundant numbers under 28123
int sumOfAbundants(vector <int> num);				//Place all numbers from 0 to 28123 in an array.  Value 1 shall be assigned to those that can
													//be written as the sum of two abundant numbers, 0 to those that cannot


int _tmain(int argc, _TCHAR* argv[])
{
	vector <int> sumsOfAbundants(28124);//28124 because 28124 there are 28124 numbers up to and including 28123 and 0
	int num[3];
	num[0] = 4;

	int sum = sumOfAbundants(sumsOfAbundants);

	cout << sum;

	while(1);
	return 0;
}

//Find the sum of all numbers that are not the sum of any two abundant numbers
int sumOfAbundants(vector <int> num){
	//Initialize all values in num to 0, the default assumption is that each one is not abundant
	for(int i = 0; i < num.size(); i ++){
		num[i] = 0;
	}

	vector <int> abundants = findAbundantNumbers();

	//Find all sums of two abundant numbers that are less than 28123
	for(int i = 0; i < abundants.size(); i ++){
		for(int j = i; j < abundants.size(); j++){
			if((abundants[i] + abundants[j]) <= 28123){
				num[abundants[i] + abundants[j]] = 1;
			}
			else break;//If i + j > 28123, they will be for the next i and j
		}
		if(i * 2 > 28123)//There are no more to be found (this should eliminate some redundancy)
			break;
	}

	int sum = 0;
	for(int i = 0; i < num.size(); i ++){
	//	cout << i << ": " << num[i] << " Sum: " << sum << endl;
		if(num[i] == 0){
			sum = sum + i;
		}
	}
	return sum;
}

//Find all abundant numbers from 12 to 28123 (it is given in the problem that the smallest abundant number is 12
vector <int> findAbundantNumbers(){
	vector <int> abundants(0);
	int size = abundants.size();

	for(int i = 12; i <= 28123; i ++){
		if(isAbundant(i)){
		//	cout << i << endl;
			size ++;
			abundants.resize(size);
			abundants[size-1] = i;//must go to size-1 because vectors are zero-indexed.
		}
	}
	//cout << size << endl;

	return abundants;
}

//Return true if a number is abundant, false otherwise
//(an abundant number is one that is less than the sum of is proper divisors (all factors apart from the number itself))
bool isAbundant(int n){
	vector <int> factors = factor(n);
	int sum = 0;//The sum of the proper divisors of n

	for(int i = 0; i < factors.size(); i++)
		sum += factors[i];

	if(sum > n)
		return true;
	else return false;
}


//Find all proper divisors of a number n.  A proper divisor of n is a number that divides n (including 1) and is not equal to n
vector <int> factor(int n){
	vector <int> ret(1);
	ret[0] = 1;//1 is a proper divisor of every number
	int j = 1;

	//There is no need to look past the squre root of n because any factor greater than the square root is paired with another
	//factor that has already been found
	for(int i = 2; i <= sqrt((double)n); i++){
		if(n % i == 0){//If n divides i, i and n/i are factors of n
			ret.resize(ret.size() + 1);
			ret[j] = i;

			if(i != n/i){//If the number is a perfect square, must not include the square root twice. 2.
				ret.resize(ret.size() + 1);
				ret[j + 1] = n/i;
			}

			j += 2;//Two factors are added at a time, so the counter must be incremented by 2
		}
	}

	return ret;
}