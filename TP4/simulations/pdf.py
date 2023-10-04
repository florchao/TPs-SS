import matplotlib.pyplot as plt
import numpy as np
import math

stationary = 120
colors = ['purple', 'magenta', 'orange', 'cyan']
velocities_dict = {}
Ns = [10,20,30]

i = 0
for j in Ns:
    i += 10
    velocities_dict[i] = []
    with open('../output/positionN' + str(j) +'K3.txt', 'r') as archive:
        dt = archive.readline()
        while float(dt) < stationary:
            for j in range(i):
                archive.readline()
            dt = archive.readline()
        for line in archive:
            cols = line.split()
            if len(cols) != 1:
                velocities_dict[i].append(float(cols[3]))

    archive.close()

for i, velocities in velocities_dict.items():
    num_particles = len(velocities)
    bins = int(math.log2(num_particles))
    p, x = np.histogram(velocities, bins)
    x = x[:-1] + (x[1] - x[0]) / 2
    plt.plot(x, [a / ((x[1] - x[0]) * num_particles) for a in p], linestyle='-', label=f'N={i}', color = colors[int(i/10) -1])
    plt.scatter(x, [a / ((x[1] - x[0]) * num_particles) for a in p], s=20, marker='o', color = colors[int(i/10) -1])


x_recta = np.linspace(9, 12)
y_recta = np.full_like(x_recta, 1 / 3)
plt.plot(x_recta, y_recta, 'r--', label='Distribución inicial')
plt.legend()
plt.xlabel('Velocidad ($\\frac{{\mathrm{cm}}}{{\mathrm{s}}})$')
plt.ylabel('PDF')
plt.xlim(7.8, 10)
plt.rcParams.update({'font.size': 24})
plt.show()