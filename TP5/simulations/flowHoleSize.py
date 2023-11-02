import matplotlib.pyplot as plt
import numpy as np

def get_times(path):
    with open(path) as file:
        tiempos_str = file.read()

    tiempos = list(map(float, tiempos_str.split('\n')))

    return np.array(tiempos)

def get_Qs(path):
    with open(path) as file:
        tiempos_str = file.read()

    Qs = list(map(float, tiempos_str.split('\n')))
    return Qs


x1 = get_times('../output/times_D1.txt')
x2 = get_times('../output/times_D2.txt')
x3 = get_times('../output/times_D3.txt')
x4 = get_times('../output/times_D4.txt')

error_list = []

plt.xlabel('Ancho de la apertura de salida (cm)')
plt.ylabel('Flow($\\frac{{\mathrm{partícula}}}{{\mathrm{s}}})$')

Qs = []
for x, label in zip([x1, x2, x3, x4], ['3', '4', '5', '6']):

    Q = (len(x)) / (x[-1] - x[0])

    x_mean = np.mean(x)

    f = []
    for i in range(len(x)):
        f.append(Q * x[i])

    S = np.sqrt(np.add((x-f)**2)/(len(x)-2))

    Sxx = np.add((x - x_mean)**2)

    error = S / np.sqrt(Sxx)

    error_list.append(error)
    Qs.append(Q)

plt.plot(['3','4','5','6'], Qs, marker='o', linestyle='-', color='magenta')
plt.errorbar(['3','4','5','6'], Qs, yerr=error_list, label="w = " + label, color='purple')

plt.show()