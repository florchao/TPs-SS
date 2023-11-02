import matplotlib.pyplot as plt
import numpy as np

def getTimes(path):
    with open(path) as file:
        timesStr = file.read()

    times = list(map(float, timesStr.split('\n')))

    return np.array(times)

x1 = getTimes('../output/timesF1.txt')
x2 = getTimes('../output/timesF2.txt')
x3 = getTimes('../output/timesF3.txt')
x4 = getTimes('../output/timesF4.txt')
x5 = getTimes('../output/timesF5.txt')
x6 = getTimes('../output/timesF6.txt')


errors = []

plt.xlabel('Frecuencia ($\\frac{{\mathrm{rad}}}{{\mathrm{s}}})$')
plt.ylabel('Flow($\\frac{{\mathrm{partícula}}}{{\mathrm{s}}})$')

Qs = []

fs = [5, 10, 15, 20, 30, 50]

for x, label in zip([x1, x2, x3, x4,x5,x6], ['5', '10', '15', '20', '30', '50']):
    Q = (len(x))/(x[-1]-x[0])
    Qs.append(Q)

    mean = np.mean(x)

    f = []
    for i in range(len(x)):
        f.append(Q*x[i])

    S = np.sqrt(np.add((x-f)**2)/(len(x)-2))

    Sxx = np.add((x - mean)**2)

    error = S / np.sqrt(Sxx)

    errors.append(error)

plt.plot(fs, Qs, marker='o', linestyle='-', color='magenta')
plt.errorbar(fs, Qs, yerr=errors, label="w = " + label, color='purple')

plt.show()