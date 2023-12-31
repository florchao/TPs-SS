import matplotlib.pyplot as plt
import numpy as np

colors = ['m', 'b', 'g', 'r', 'c', 'y']
def getTimes(path):
    with open(path) as file:
        timesStr = file.readlines()

    times = []
    for line in timesStr:
        times.append(float(line))

    return np.array(times)


x1 = getTimes('../output/timesF1.txt')
x2 = getTimes('../output/timesF2.txt')
x3 = getTimes('../output/timesF3.txt')
x4 = getTimes('../output/timesF4.txt')
x5 = getTimes('../output/timesF5.txt')
x6 = getTimes('../output/timesF6.txt')


for x, label, color in zip([x1, x2, x3, x4, x5,x6], ['5', '10', '15', '20', '30', '50'], colors):
    count, limits = np.histogram(x, bins=1000)

    accumCount = np.cumsum(count)

    plt.step(limits[:-1], accumCount, where='post', label=label + '($\\frac{{\mathrm{rad}}}{{\mathrm{s}}})$', color=color)

plt.xlabel('Tiempo (s)')
plt.ylabel('Curva de descarga (particulas)')
plt.legend()

plt.show()
