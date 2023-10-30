import matplotlib.pyplot as plt
import numpy as np

colors = ['m', 'b', 'g', 'r', 'c', 'y']
def getTimes(path):
    with open(path) as file:
        timesStr = file.readlines()

    times = []
    for line in timesStr:
        times.append(float(line))

    data = np.array(times)

    return data


x1 = getTimes('../output/times_F17.txt')
x2 = getTimes('../output/times_F18.txt')
x3 = getTimes('../output/times_F19.txt')
x4 = getTimes('../output/times_F10.txt')

for x, label, color in zip([x1, x2, x3, x4], ['5', '10', '15', '20'], colors):
    count, limits = np.histogram(x, bins=1000)

    accumCount = np.cumsum(count)

    plt.step(limits[:-1], accumCount, where='post', label=label + " Hz", color=color)

plt.xlabel('Tiempo (s)')
plt.ylabel('Curva de descarga (particulas)')
plt.legend()

plt.show()
