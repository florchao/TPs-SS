import matplotlib.pyplot as plt
import numpy as np

stationary = 120
colors = ['m', 'b', 'g', 'r', 'c', 'y']
vels = {}
Ns = [10,20,30]

for j in Ns:
    vels[j] = []
    with open('../output/positionN' + str(j) +'K3.txt', 'r') as archive:
        dt = archive.readline()
        while float(dt) < stationary:
            for j in range(j):
                archive.readline()
            dt = archive.readline()
        for line in archive:
            cols = line.split()
            if len(cols) != 1:
                vels[j].append(float(cols[2]))

    archive.close()

for j, velocities in vels.items():
    num_particles = len(velocities)
    bins = int(np.log2(num_particles))
    p, x = np.histogram(velocities, bins)
    x = x[:-1] + (x[1] - x[0]) / 2
    plt.plot(x, [a / ((x[1] - x[0]) * num_particles) for a in p], linestyle='-', label=f'N={j}', color = colors[int(j/10) -1])
    plt.scatter(x, [a / ((x[1] - x[0]) * num_particles) for a in p], s=20, marker='o', color = colors[int(j/10) -1])


x = np.linspace(9, 12)
y = np.full_like(x, 1 / 3)
plt.plot(x, y, 'r--', label='Distribución inicial')
plt.legend()
plt.xlabel('Velocidad ($\\frac{{\mathrm{cm}}}{{\mathrm{s}}})$')
plt.ylabel('PDF')
plt.xlim(7.8, 10)
plt.rcParams.update({'font.size': 24})
plt.show()