import numpy as np
import matplotlib.pyplot as plt
import math

N = [200]
Ls = [0.03, 0.05, 0.07, 0.09]
file_list = []
color_list = ['b', 'g', 'r', 'c', 'm']
data = {}

file_list = []
for i in range(len(Ls)):
    file_list.append(f"../files/output/finalPressure{N}L{Ls[i]}.txt")
pL=[]
pressure=[]
stds=[]
for idx, file_name in enumerate(file_list):
    pressure=[]
    with open(file_name, "r") as file:
        lines = file.readlines()
        pressure.append( float(lines[0].strip().split()[0]))
        pL.append(2*np.mean(pressure))
        stds.append(np.std(pressure))
x = [abs(1/(0.09*0.09 + 0.09*L) - 1/(0.09*0.09 + 0.09*0.03)) for L in Ls]
c= [x* 0.0001 for x in range(250, 400)]
pres1 = pL[0]
pressure_perL = [abs(yi - pres1) for yi in pL]
ec = []
for ci in c:
    ec.append(sum([math.pow(yi-pL[0] - ci*xi,2) for xi, yi in zip(x, pL)]))
plt.plot(c, ec, marker='o', markersize=0.1 ,linestyle='-', color='magenta', label=f'N= {N} ')
for(eci, ci) in zip(ec, c):
    if eci == min(ec):
        print(ci)

plt.xlabel('Pendiente')
plt.ylabel('Error Cuadrático Medio (MSE)')
plt.legend()
plt.savefig('../files/output/regressionError.png')

plt.cla()
