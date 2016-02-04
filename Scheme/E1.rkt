;Determine if the input n is a multiple of five
(define (mul-five n)
 (cond((equal? (modulo n 5) 0)
  #t)
(else #f)))

;Determine if the input n is a multiple of three
(define (mul-three n)
 (cond((equal? (modulo n 3) 0)
  #t)
(else #f)))

;Determine if the input is a multiple of three OR five
(define (mul n)
  (cond ((mul-five n) #t)
        ((mul-three n) #t)
        (else #f))
)

(define (check n)
  (cond ((counter n)
          n)
        (else (check (+ n 1)))))
          

;Check to see if 1000 iterations have been reached
(define (counter n)
 (cond((equal? 1000 n)
  #t)
(else #f)))

(define (sum-mul-three-five n sum)
  (cond ((counter n) sum)
  ((mul n) (sum-mul-three-five (+ n 1) (+ n sum)))
  (else (sum-mul-three-five (+ n 1) sum))
  )
)

(define (euler-1)
  (sum-mul-three-five 1 0))