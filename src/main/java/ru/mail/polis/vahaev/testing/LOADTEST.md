## Нагрузочное тестирование - 3 этап

Нагрузочное тестирование произведено с помощью утилиты wrk, скрипты лежат
в папке testing. Тестирование происходило в течение 1 минуты, в режимах 1/2/4 потока и 1/2/4 соединения,
с помощью двух методов: GET, PUT.
Тестирование было произведено таким образом: Запускался тест "Put без перезаписи", затем "Get без повторов". После
этого перезапускался сервер и производилось такое же тестирование, только для другого количества потоков и соединений.
Затем был произведён тест "Put с перезаписью", после которого запускался тест "Get с повторами". Потом сервер перезапускался
и производилось то же тестирование с другим количеством потоков и соединений.

### До оптимизаций

### Put без перезаписи:

**1 поток, 1 соединение**

    sudo wrk --latency -t1 -c1 -d1m -s putNotOverwrite.lua http://localhost:8080
    Running 1m test @ http://localhost:8080
	  1 threads and 1 connections
	  Thread Stats   Avg      Stdev     Max   +/- Stdev
		Latency    10.40ms    6.17ms 145.68ms   97.26%
		Req/Sec    51.26      7.84    70.00     70.60%
	  Latency Distribution
		50%    9.22ms
		75%   11.66ms
		90%   13.88ms
		99%   26.23ms
	  3078 requests in 1.00m, 282.55KB read
	Requests/sec:     51.23
	Transfer/sec:      4.70KB

**2 потока, 2 соединения**

    sudo wrk --latency -t2 -c2 -d1m -s putNotOverwrite.lua http://localhost:8080
    Running 1m test @ http://localhost:8080
	  2 threads and 2 connections
	  Thread Stats   Avg      Stdev     Max   +/- Stdev
		Latency    11.22ms    8.69ms 184.21ms   97.32%
		Req/Sec    42.19      5.78    60.00     72.25%
	  Latency Distribution
		50%   10.15ms
		75%   12.44ms
		90%   14.84ms
		99%   29.07ms
	  5070 requests in 1.00m, 465.41KB read
	Requests/sec:     84.47
	Transfer/sec:      7.75KB
	
**4 потока, 4 соединения**

    sudo wrk --latency -t4 -c4 -d1m -s putNotOverwrite.lua http://localhost:8080
    Running 1m test @ http://localhost:8080
	  4 threads and 4 connections
	  Thread Stats   Avg      Stdev     Max   +/- Stdev
		Latency     9.30ms    8.60ms 113.18ms   88.12%
		Req/Sec    41.02     15.07   110.00     75.75%
	  Latency Distribution
		50%    6.96ms
		75%   12.69ms
		90%   19.16ms
		99%   41.32ms
	  9772 requests in 1.00m, 0.88MB read
	Requests/sec:    162.62
	Transfer/sec:     14.93KB

### Get без повторов:

**1 поток, 1 соединение**

    sudo wrk --latency -t1 -c1 -d1m -s getNotRepeats.lua http://localhost:8080
    Running 1m test @ http://localhost:8080
      1 threads and 1 connections
	  Thread Stats   Avg      Stdev     Max   +/- Stdev
		Latency     1.38ms    1.61ms  36.18ms   92.09%
		Req/Sec   793.48    406.40     1.75k    66.11%
	  Latency Distribution
		50%    0.94ms
		75%    1.74ms
		90%    2.71ms
		99%    7.49ms
	  47415 requests in 1.00m, 16.32MB read
	  Non-2xx or 3xx responses: 44336
	Requests/sec:    788.96
	Transfer/sec:    278.00KB

**2 потока, 2 соединения**

    sudo wrk --latency -t2 -c2 -d1m -s getNotRepeats.lua http://localhost:8080
	Running 1m test @ http://localhost:8080
	  2 threads and 2 connections
	  Thread Stats   Avg      Stdev     Max   +/- Stdev
		Latency     3.78ms    3.70ms  46.79ms   88.70%
		Req/Sec   301.80    158.52     0.98k    72.69%
	  Latency Distribution
		50%    2.71ms
		75%    4.89ms
		90%    7.97ms
		99%   18.26ms
	  35981 requests in 1.00m, 23.04MB read
	  Non-2xx or 3xx responses: 30904
	Requests/sec:    599.11
	Transfer/sec:    392.82KB
	
**4 потока, 4 соединения**

    sudo wrk --latency -t4 -c4 -d1m -s getNotRepeats.lua http://localhost:8080
    Running 1m test @ http://localhost:8080
	  4 threads and 4 connections
	  Thread Stats   Avg      Stdev     Max   +/- Stdev
		Latency     4.20ms    3.37ms  49.83ms   80.08%
		Req/Sec   253.93    128.77   670.00     71.16%
	  Latency Distribution
		50%    3.37ms
		75%    5.81ms
		90%    8.34ms
		99%   15.29ms
	  60707 requests in 1.00m, 43.68MB read
	  Non-2xx or 3xx responses: 50904
	Requests/sec:   1010.77
	Transfer/sec:    744.77KB

### Put с перезаписью:

**1 поток, 1 соединение**

    sudo wrk --latency -t1 -c1 -d1m -s putOverwrite.lua http://localhost:8080
    Running 1m test @ http://localhost:8080
	  1 threads and 1 connections
	  Thread Stats   Avg      Stdev     Max   +/- Stdev
		Latency     8.07ms    6.19ms 124.40ms   92.46%
		Req/Sec    58.59     16.77   101.00     59.21%
	  Latency Distribution
		50%    6.54ms
		75%    8.97ms
		90%   12.90ms
		99%   30.16ms
	  3503 requests in 1.00m, 321.56KB read
	Requests/sec:     58.36
	Transfer/sec:      5.36KB

**2 потока, 2 соединения**

    sudo wrk --latency -t2 -c2 -d1m -s putOverwrite.lua http://localhost:8080
    Running 1m test @ http://localhost:8080
	  2 threads and 2 connections
	  Thread Stats   Avg      Stdev     Max   +/- Stdev
		Latency     4.33ms   12.32ms 249.00ms   98.87%
		Req/Sec    97.91     13.54   121.00     82.56%
	  Latency Distribution
		50%    3.10ms
		75%    3.57ms
		90%    4.35ms
		99%   22.68ms
	  11692 requests in 1.00m, 1.05MB read
	Requests/sec:    194.59
	Transfer/sec:     17.86KB
	
**4 потока, 4 соединения**

    sudo wrk --latency -t4 -c4 -d1m -s putOverwrite.lua http://localhost:8080
    Running 1m test @ http://localhost:8080
	  4 threads and 4 connections
	  Thread Stats   Avg      Stdev     Max   +/- Stdev
		Latency     8.13ms    5.71ms 106.43ms   93.91%
		Req/Sec    59.06     10.32    90.00     72.58%
	  Latency Distribution
		50%    7.13ms
		75%    8.71ms
		90%   11.93ms
		99%   28.77ms
	  14179 requests in 1.00m, 1.27MB read
	Requests/sec:    236.00
	Transfer/sec:     21.66KB

### Get с повторами:

**1 поток, 1 соединение**
    
    sudo wrk --latency -t1 -c1 -d1m -s getRepeats.lua http://localhost:8080
    Running 1m test @ http://localhost:8080
	  1 threads and 1 connections
	  Thread Stats   Avg      Stdev     Max   +/- Stdev
		Latency     1.13ms    1.55ms  38.39ms   94.23%
		Req/Sec     0.97k   230.77     1.39k    67.06%
	  Latency Distribution
		50%  742.00us
		75%    1.24ms
		90%    2.06ms
		99%    7.32ms
	  57759 requests in 1.00m, 229.92MB read
	Requests/sec:    962.63
	Transfer/sec:      3.83MB

**2 потока, 2 соединения**
    
    sudo wrk --latency -t2 -c2 -d1m -s getRepeats.lua http://localhost:8080
    Running 1m test @ http://localhost:8080
	  2 threads and 2 connections
	  Thread Stats   Avg      Stdev     Max   +/- Stdev
		Latency   524.04us  289.24us  21.68ms   96.32%
		Req/Sec     1.85k   188.75     2.22k    74.67%
	  Latency Distribution
		50%  492.00us
		75%  547.00us
		90%  649.00us
		99%    1.13ms
	  220744 requests in 1.00m, 0.86GB read
	Requests/sec:   3678.63
	Transfer/sec:     14.64MB
	
**4 потока, 4 соединения**
    
    sudo wrk --latency -t4 -c4 -d1m -s getRepeats.lua http://localhost:8080
    Running 1m test @ http://localhost:8080
	  4 threads and 4 connections
	  Thread Stats   Avg      Stdev     Max   +/- Stdev
		Latency     1.00ms  732.11us  28.78ms   94.51%
		Req/Sec     1.03k   176.84     1.32k    72.08%
	  Latency Distribution
		50%    0.88ms
		75%    1.14ms
		90%    1.45ms
		99%    3.46ms
	  247221 requests in 1.00m, 0.96GB read
	Requests/sec:   4116.41
	Transfer/sec:     16.39MB
    
### После проведения оптимизаций

### Put без перезаписи:

**1 поток, 1 соединение**

    sudo wrk --latency -t1 -c1 -d1m -s putNotOverwrite.lua http://localhost:8080
	Running 1m test @ http://localhost:8080
	  1 threads and 1 connections
	  Thread Stats   Avg      Stdev     Max   +/- Stdev
		Latency     5.11ms   12.23ms 276.57ms   98.17%
		Req/Sec   100.97     17.80   131.00     66.44%
	  Latency Distribution
		50%    3.70ms
		75%    4.46ms
		90%    5.28ms
		99%   37.67ms
	  6026 requests in 1.00m, 553.17KB read
	Requests/sec:    100.35
	Transfer/sec:      9.21KB
  

**2 потока, 2 соединения**

    sudo wrk --latency -t2 -c2 -d1m -s putNotOverwrite.lua http://localhost:8080
    Running 1m test @ http://localhost:8080
	  2 threads and 2 connections
	  Thread Stats   Avg      Stdev     Max   +/- Stdev
		Latency    12.47ms   45.09ms 517.80ms   96.43%
		Req/Sec    78.68     15.50   110.00     75.77%
	  Latency Distribution
		50%    4.47ms
		75%    5.18ms
		90%    6.68ms
		99%  279.52ms
	  9193 requests in 1.00m, 843.89KB read
	Requests/sec:    152.98
	Transfer/sec:     14.04KB
   
**4 потока, 4 соединения**

	sudo wrk --latency -t4 -c4 -d1m -s putNotOverwrite.lua http://localhost:8080
	Running 1m test @ http://localhost:8080
	  4 threads and 4 connections
	  Thread Stats   Avg      Stdev     Max   +/- Stdev
		Latency    18.48ms   54.02ms 681.57ms   95.94%
		Req/Sec    52.78     11.09    89.00     75.32%
	  Latency Distribution
		50%    8.08ms
		75%   10.74ms
		90%   14.97ms
		99%  321.28ms
	  12305 requests in 1.00m, 1.10MB read
	Requests/sec:    204.73
	Transfer/sec:     18.79KB
   
### Get без повторов:

**1 поток, 1 соединение**

    sudo wrk --latency -t1 -c1 -d1m -s getNotRepeats.lua http://localhost:8080
    Running 1m test @ http://localhost:8080
	  1 threads and 1 connections
	  Thread Stats   Avg      Stdev     Max   +/- Stdev
		Latency   777.38us  590.74us  11.08ms   86.01%
		Req/Sec     1.31k     1.01k    4.21k    86.17%
	  Latency Distribution
		50%  659.00us
		75%    1.10ms
		90%    1.50ms
		99%    2.69ms
	  78509 requests in 1.00m, 30.63MB read
	  Non-2xx or 3xx responses: 72482
	Requests/sec:   1307.39
	Transfer/sec:    522.27KB
     


**2 потока, 2 соединения**

    sudo wrk --latency -t2 -c2 -d1m -s getNotRepeats.lua http://localhost:8080
    Running 1m test @ http://localhost:8080
	  2 threads and 2 connections
	  Thread Stats   Avg      Stdev     Max   +/- Stdev
		Latency     1.13ms  827.38us  18.52ms   82.40%
		Req/Sec     0.92k   596.30     2.40k    79.33%
	  Latency Distribution
		50%    0.94ms
		75%    1.59ms
		90%    2.11ms
		99%    3.85ms
	  109542 requests in 1.00m, 45.79MB read
	  Non-2xx or 3xx responses: 100347
	Requests/sec:   1823.47
	Transfer/sec:    780.52KB
     
**4 потока, 4 соединения**

	sudo wrk --latency -t4 -c4 -d1m -s getNotRepeats.lua http://localhost:8080
	Running 1m test @ http://localhost:8080
	  4 threads and 4 connections
	  Thread Stats   Avg      Stdev     Max   +/- Stdev
		Latency     1.32ms    0.95ms  21.67ms   82.69%
		Req/Sec   795.14    374.35     1.81k    52.62%
	  Latency Distribution
		50%    1.03ms
		75%    1.69ms
		90%    2.49ms
		99%    4.40ms
	  190156 requests in 1.00m, 65.54MB read
	  Non-2xx or 3xx responses: 177781
	Requests/sec:   3164.70
	Transfer/sec:      1.09MB
	 
### Put с перезаписью:

**1 поток, 1 соединение**

    sudo wrk --latency -t1 -c1 -d1m -s putOverwrite.lua http://localhost:8080
	Running 1m test @ http://localhost:8080
	  1 threads and 1 connections
	  Thread Stats   Avg      Stdev     Max   +/- Stdev
		Latency     4.95ms   12.85ms 306.27ms   99.06%
		Req/Sec    90.43     23.53   131.00     63.06%
	  Latency Distribution
		50%    3.50ms
		75%    4.57ms
		90%    6.34ms
		99%   16.66ms
	  5393 requests in 1.00m, 495.06KB read
	Requests/sec:     89.77
	Transfer/sec:      8.24KB
 

**2 потока, 2 соединения**

    sudo wrk --latency -t2 -c2 -d1m -s putOverwrite.lua http://localhost:8080
	Running 1m test @ http://localhost:8080
	  2 threads and 2 connections
	  Thread Stats   Avg      Stdev     Max   +/- Stdev
		Latency     3.59ms    3.88ms 111.19ms   98.89%
		Req/Sec    95.62     12.43   115.00     77.11%
	  Latency Distribution
		50%    3.15ms
		75%    3.63ms
		90%    4.33ms
		99%    7.82ms
	  11455 requests in 1.00m, 1.03MB read
	Requests/sec:    190.74
	Transfer/sec:     17.51KB

	 
**4 потока, 4 соединения**

    sudo wrk --latency -t4 -c4 -d1m -s putOverwrite.lua http://localhost:8080
	Running 1m test @ http://localhost:8080
	  4 threads and 4 connections
	  Thread Stats   Avg      Stdev     Max   +/- Stdev
		Latency     8.09ms   13.66ms 318.75ms   98.73%
		Req/Sec    59.40      9.10    70.00     70.48%
	  Latency Distribution
		50%    6.55ms
		75%    8.33ms
		90%   10.82ms
		99%   25.29ms
	  14240 requests in 1.00m, 1.28MB read
	Requests/sec:    236.99
	Transfer/sec:     21.75KB


### Get с повторами:

**1 поток, 1 соединение**
    
    sudo wrk --latency -t1 -c1 -d1m -s getRepeats.lua http://localhost:8080
	Running 1m test @ http://localhost:8080
	  1 threads and 1 connections
	  Thread Stats   Avg      Stdev     Max   +/- Stdev
		Latency   148.13us  552.00us  30.83ms   99.21%
		Req/Sec     6.82k     0.97k    7.85k    91.51%
	  Latency Distribution
		50%  110.00us
		75%  126.00us
		90%  150.00us
		99%  570.00us
	  407636 requests in 1.00m, 1.58GB read
	Requests/sec:   6782.61
	Transfer/sec:     27.00MB
 

**2 потока, 2 соединения**
    
    sudo wrk --latency -t2 -c2 -d1m -s getRepeats.lua http://localhost:8080
	Running 1m test @ http://localhost:8080
	  2 threads and 2 connections
	  Thread Stats   Avg      Stdev     Max   +/- Stdev
		Latency   204.68us  201.34us  11.93ms   96.18%
		Req/Sec     4.46k   645.60     7.19k    90.59%
	  Latency Distribution
		50%  173.00us
		75%  207.00us
		90%  273.00us
		99%  799.00us
	  533044 requests in 1.00m, 2.07GB read
	Requests/sec:   8869.42
	Transfer/sec:     35.31MB
 

**4 потока, 4 соединения**
    
    sudo wrk --latency -t4 -c4 -d1m -s getRepeats.lua http://localhost:8080
	Running 1m test @ http://localhost:8080
	  4 threads and 4 connections
	  Thread Stats   Avg      Stdev     Max   +/- Stdev
		Latency   349.36us  315.91us  13.44ms   94.92%
		Req/Sec     2.79k   493.55     3.46k    82.96%
	  Latency Distribution
		50%  293.00us
		75%  383.00us
		90%  508.00us
		99%    1.41ms
	  666108 requests in 1.00m, 2.59GB read
	Requests/sec:  11099.55
	Transfer/sec:     44.18MB
 

### Итоги оптимизации:

С помощью оптимизации удалось добиться уменьшения задержки и увеличения количества запросов.
"Put без перезаписи" - после оптимизации количество запросов увеличилось в 1,5-2 раза
"Get без повторов"   - после оптимизации количество запросов увеличилось в 1,5-3 раза
"Put с перезаписью"  - после оптимизации выявилось незначительно увеличение запросов
"Get с повторами"    - после оптимизации количество запросов увеличилось примерно в 3 раза