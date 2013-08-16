;Return the sum of all even fibonacci numbers from two to four-million

;Get the next number of the fibonacci sequence
(define (next-fib x y)
  (+ x y))

;Get the sum of all even numbers in the fibonacci sequence between 0
;and 'stop', starting at x and y
(define (sum-fib x y sum stop)
  (cond ((>= y stop) sum)
        ((equal? (modulo y 2) 1) (sum-fib y (next-fib x y) sum stop))
  (else (sum-fib y (next-fib x y) (+ sum y) stop))))

;Get the sum of all even fibonacci numbers in the sequence between
;1 and 4000000
(define (e-2)
  (sum-fib 1 2 0 4000000))