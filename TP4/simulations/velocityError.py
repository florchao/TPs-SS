import matplotlib.pyplot as plt
import numpy as np

def parseParams(file_path):
    with open(file_path, 'r') as file:
        lines = file.readlines()

    particles = {}
    time = None
    for line in lines:
        data = line.split("\t")
        if len(data) == 1:
            time = float(data[0])
            particles[time] = []
        else:
            particle = {
                'vx': float(data[2]),
            }

            particles[time].append(particle)

    return particles

Ns = [5, 10, 15, 20, 25, 30]
particles = {}
arrayVels = {}

for N in Ns:
    data = parseParams("../output/orderPositionN" + str(N) +"K3.txt")
    velProms = []
    arrayVels[N] = []
    for i in data.keys():
        if i < 120.00:
            continue
        vels = 0

        for current_particle in data[i]:
            auxParticle = current_particle['vx']
            vels += current_particle['vx']
            arrayVels[N].append(auxParticle)

        velProms.append(vels/N)

    particles[N] = velProms

proms = {N: sum(vels) / len(vels) for N, vels in particles.items()}
auxProms = list(proms.values())

aux_prom = []
error = []
for i in range(len(Ns)):
    aux_prom.append(auxProms[i])
    aux = (arrayVels[Ns[i]])
    error.append(np.std(aux))


plt.plot(Ns, aux_prom, linestyle='-', color='magenta', label='Línea de Unión')
plt.errorbar(Ns, aux_prom, yerr=error, fmt='o', color='purple', capsize=6)

plt.xlabel('N')
plt.ylabel('Velocidad Promedio ($\\frac{{\mathrm{cm}}}{{\mathrm{s}}})$')
plt.rcParams.update({'font.size': 24})
plt.show()

