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
plt.ylabel('Caudal ($\\frac{{\mathrm{partícula}}}{{\mathrm{s}}})$')

for x, label in zip([x1, x2, x3, x4], ['3', '4', '5', '6']):

    Q = (len(x)) / (x[-1] - x[0])

    x_mean = np.mean(x)

    f = []
    for i in range(len(x)):
        f.append(Q * x[i])

    S = np.sqrt(np.sum((x-f)**2)/(len(x)-2))

    Sxx = np.sum((x - x_mean)**2)

    error = S / np.sqrt(Sxx)

    error_list.append(error)
    plt.errorbar(int(label), Q, yerr=error, label="D = " + label, color='purple')
    plt.plot(int(label), Q, linestyle='-', marker='o', color='magenta')

plt.show()