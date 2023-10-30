import matplotlib.pyplot as plt
import numpy as np

radius = 0.0098440453
density = 3300
g = 0.05 ** 0.5

def beverloo(x, c):
    aux = x - c * radius
    return density * g * (aux ** 1.5)

def beverlooError(Qs, Ds, c):
    result = 0
    for q, d in zip(Qs, Ds):
        b = beverloo(d, c)
        result = result + (q - b) ** 2
    return result


def getQs(path):
    with open(path) as file:
        timesStr = file.read()

    return list(map(float, timesStr.split('\n')))


Cs = [num / 100.0 for num in range(0, 200, 1)]
Qs = getQs('../output/caudals_D16.txt')
Qs.append(getQs('../output/caudals_D15.txt')[0])
Qs.append(getQs('../output/caudals_D14.txt')[0])
Qs.append(getQs('../output/caudals_D13.txt')[0])

beverloo_err = [beverlooError(Qs, [0.03 + 2 * radius, 0.04 + 2 * radius, 0.05 + 2 * radius, 0.06 + 2 * radius], c) for c
                in Cs]
plt.plot(Cs, beverloo_err, color='magenta')

c = Cs[np.argmin(beverloo_err)]
plt.scatter(Cs[np.argmin(beverloo_err)], beverloo_err[np.argmin(beverloo_err)], color='purple')

plt.xlabel('Parámetro libre c')
plt.ylabel('Error')

plt.show()

x = [0.001 * i for i in range(20, 100)]
y = [beverloo(x_i, c) for x_i in x]

plt.plot(x, y, color='magenta', label='Beverloo')
plt.scatter([0.03 + 2 * radius, 0.04 + 2 * radius, 0.05 + 2 * radius, 0.06 + 2 * radius], Qs, color='purple', label='Resultados')
plt.ylabel('Caudal (partículas/s)')
plt.xlabel('Apertura (cm)')
plt.legend()
plt.show()
