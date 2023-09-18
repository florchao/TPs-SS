import numpy as np
import matplotlib.pyplot as plt

WIDTH = 0.09
N = 200
L = [0.03, 0.05, 0.07, 0.09]
file_list = []
color_list = ['b', 'g', 'r', 'c', 'm']

file_list = []
for i in range(len(L)):
    file_list.append(f"../files/output/finalPressure{N}L{L[i]}.txt")
pressure_perL=[]
pressure=[]
stds=[]
for idx, file_name in enumerate(file_list):
    pressure=[]
    with open(file_name, "r") as file:
        lines = file.readlines()
        pressure.append( float(lines[0].strip().split()[0]))
        pressure_perL.append(np.mean(pressure))
        stds.append(np.std(pressure))
x=[]
x.append(0)
x = [1/(WIDTH*WIDTH + WIDTH*L) for L in L]
plt.scatter(x, pressure_perL, marker='o', linestyle='-', color='purple', label=f'N= {N} ')
plt.errorbar(x, pressure_perL, yerr=stds, fmt='o', color='purple', ecolor='purple')

t = np.arange(len(x))

slope, _ = np.polyfit(x[0:len(t)], pressure_perL, 1)

y_regresion = np.array(x) *  slope
plt.plot(x, y_regresion, color='magenta')


plt.xlabel('Inversa del área ($\\frac{1}{m^2}$)')
plt.ylabel('Presión ($\\frac{kg}{m \\cdot s^2}$)')
plt.legend()
plt.savefig('../files/output/regresionPvsA-1.png')
plt.cla()