(define (factor-exponent-pairs l pairs)
 (cond
   ((null? l) pairs);If there is nothing left of l, return what we have
   ((in? (car l) pairs) 
)
;Returns true if n is in l and false otherwise
(define (in? n l)
  (cond
    ((null? l) #f);Base case: If the list is empty, n is not in l
    ((equal? (caar l) n) #t);It is in the list
    (else (in? n (cdr l)))
    )
  )

;Return a list of the factors of n
(define (factor n i)
  (cond 
    ((> i (sqrt n)) '())
    ((factor? n i) (append (list i (/ n i)) (factor n (+ i 1))))
    (else (factor n (+ i 1)))
    )
  )

;Takes a list of factors of a number and places the ones that are prime into a new list
(define (get-prime-factors l)
  (cond
    ((null? l) '())
    ((prime? (car l)) (append (list (car l)) (get-prime-factors (cdr l))))
    (else (get-prime-factors (cdr l))))
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
    ((equal? 2 n) #t)
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
