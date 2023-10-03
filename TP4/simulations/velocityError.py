import matplotlib.pyplot as plt
import numpy as np

def parseParams(file_path):
    with open(file_path, 'r') as file:
        lines = file.readlines()

    particles_data = {}
    time = None
    for line in lines:
        data = line.split("\t")
        if len(data) == 1:
            time = float(data[0])
            particles_data[time] = []
        else:
            particle = {
                'vx': float(data[2]),
            }

            particles_data[time].append(particle)

    return particles_data

Ns = [5, 10, 15, 20, 25, 30]
particles_data = {}
arrayVels = {}

for N in Ns:
    data = parseParams("../output/positionN" + str(N) +"K3.txt")
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

    particles_data[N] = velProms

proms = {N: sum(vels) / len(vels) for N, vels in particles_data.items()}
auxProms = list(proms.values())

aux_prom = []
error = []
for i in range(len(Ns)):
    aux_prom.append(auxProms[i])
    aux = (arrayVels[Ns[i]])
    error.append(np.std(aux))


plt.plot(Ns, aux_prom, linestyle='-', color='pink', label='Línea de Unión')
plt.errorbar(Ns, aux_prom, yerr=error, fmt='o', color='purple', capsize=6)

plt.xlabel('N')
plt.ylabel('Velocidad Promedio ($\\frac{{\mathrm{cm}}}{{\mathrm{s}}})$')
plt.show()

