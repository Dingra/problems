;The problem: Find the largest prime factor of the number 600851475143

;Solve the problem
(define (euler-3)
  (largest-prime-factor 600851475143)
  )

;Find the largest prime factor of a number n
(define (largest-prime-factor n)
  (find-max-prime (find-factors n) 0)
  )

;Find all factors of a number n and put them in a list
(define (find-factors n)
  (find-factors-helper n 1 '()))

;Do the dirty work to find all factors of a number n
(define (find-factors-helper n m factors)
  (cond
    ((> m (sqrt n));base case, all factors have been found
     factors)
    
    ;If m divides n evenly it is a prime factor.  Add it to the list and go to the next one
    ((integer? (/ n m));
     (find-factors-helper n (+ 1 m) (append factors (list m (/ n m))))
  )
    ;Otherwise, just go to the next one
    (else (find-factors-helper n (+ 1 m) factors)))
  )

;Check to see if a number n is prime
(define (prime? n)
  (cond
    ((prime-helper n 2) #f)
    (else #t)))

;Do Check all numbers m up to the square root of n to see if they are factors.  f one is found, return true, otherwise, return false
(define (prime-helper n m)
  (cond
    ((factor? n m) #t);base case
    ((> m (sqrt n)) #f)
    (else (prime-helper n (+ m 1)))
    )
  )

;Determine if m is a factor of m
(define (factor? n m)
  (cond
    ((integer? (/ n m)) #t)
    (else #f)
    )
  )

;Iterate through a list, keeping track of the largest prime number in that list
(define (find-max-prime l max)
  (cond
    ((equal? l '()) max);base case, at the end of the list, return max
    ((> (car l) max)
     (cond
       ((prime? (car l))
         (find-max-prime (cdr l) (car l))
        )
        (else
         (find-max-prime (cdr l) max))
        ))
    
    (else
     (find-max-prime (cdr l) max))
    )
  )

