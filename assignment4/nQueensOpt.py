# SAT solver
# N-Queens
from z3 import *
import time

# Number of Queens
print("N: ")
N = int(input())

start = time.time()
# 변수의 절댓값을 z3 type로 반환
#def abs_z3(a):
    #return If(a >= 0, a, -a)

# columns = [x0,...,xn], lines = [y0,...,yn]
#X = IntVector("x", N)
X = [Int('x_%s' % (i)) for i in range(N)]
s = Solver()

# [constraint] 1 ~ N-1 외의 숫자는 나타나지 않도록 범위 제한
s.add([And(X[i] >= 1, X[i] <= N) for i in range(N)])
 
# [constraint] 같은 열/행에는 하나의 queen만 올 수 있고, 대각선상에는 올 수 없는 n-queens의 규칙 제한
s.add([And(X[i] != X[j], X[i]+i != X[j]+j, X[i]-i != X[j]-j) for i in range(N) for j in range(i)])
 
if s.check() == sat:
    m = s.model()
    r = [m.evaluate(X[i]) for i in range(N)]
    print(r)

print("elapsed time: ", time.time() - start, " sec")