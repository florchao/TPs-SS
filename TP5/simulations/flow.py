import matplotlib.pyplot as plt
import numpy as np

def getTimes(path):
    with open(path) as file:
        timesStr = file.read()

    times = list(map(float, timesStr.split('\n')))

    return np.array(times)

x1 = getTimes('../output/times_F17.txt')
x2 = getTimes('../output/times_F18.txt')
x3 = getTimes('../output/times_F19.txt')
x4 = getTimes('../output/times_F10.txt')

errors = []

plt.xlabel('Frecuencia (Hz)')
plt.ylabel('Caudal (partícula/s)')

for x, label in zip([x1, x2, x3, x4], ['5', '10', '15', '20']):

    Q = (len(x))/(x[-1]-x[0])

    mean = np.mean(x)

    f = []
    for i in range(len(x)):
        f.append(Q*x[i])

    S = np.sqrt(np.sum((x-f)**2)/(len(x)-2))

    Sxx = np.sum((x - mean)**2)

    error = S / np.sqrt(Sxx)

    errors.append(error)

#no me agrega la linea, no se porque
    plt.plot(int(label), Q, marker='o', linestyle='-', color='magenta')
    plt.errorbar(int(label), Q, yerr=error, label="w = " + label, color='purple')

plt.show()