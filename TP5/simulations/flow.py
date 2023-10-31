import matplotlib.pyplot as plt
import numpy as np

def getTimes(path):
    with open(path) as file:
        timesStr = file.read()

    times = list(map(float, timesStr.split('\n')))

    return np.array(times)

x1 = getTimes('../output/times_F1.txt')
x2 = getTimes('../output/times_F2.txt')
x3 = getTimes('../output/times_F3.txt')
x4 = getTimes('../output/times_F4.txt')
x5 = getTimes('../output/times_F5.txt')
x6 = getTimes('../output/times_F6.txt')


errors = []

plt.xlabel('Frecuencia ($\\frac{{\mathrm{rad}}}{{\mathrm{s}}})$')
plt.ylabel('Caudal ($\\frac{{\mathrm{partícula}}}{{\mathrm{s}}})$')

for x, label in zip([x1, x2, x3, x4,x5,x6], ['5', '10', '15', '20', '30', '50']):

    Q = (len(x))/(x[-1]-x[0])

    mean = np.mean(x)

    f = []
    for i in range(len(x)):
        f.append(Q*x[i])

    S = np.sqrt(np.sum((x-f)**2)/(len(x)-2))

    Sxx = np.sum((x - mean)**2)

    error = S / np.sqrt(Sxx)

    errors.append(error)

    plt.plot(int(label), Q, marker='o', linestyle='-', color='magenta')
    plt.errorbar(int(label), Q, yerr=error, label="w = " + label, color='purple')

plt.show()