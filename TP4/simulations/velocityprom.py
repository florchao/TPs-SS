import matplotlib.pyplot as plt

Ns = [5,10, 15,20,25,30]
colors = ['purple', 'pink', 'orange', 'lightgreen', 'lightblue', 'magenta']
index = 0
for N in Ns:
    with open("../output/positionN" + str(N) +"K3.txt", 'r') as file:
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
    aux = []
    for i in particles_data.keys():
        vel_difference = 0

        for current_particle in particles_data[i]:
            vel_difference += current_particle['vx']

        aux.append(vel_difference/N)

    plt.plot([i * 0.1 for i in range(0, 1801)], aux, linestyle='-', color=colors[index],label=f'N = {N}')
    index += 1
plt.xlabel('Tiempo (s)')
plt.ylabel('Velocidad Promedio ($\\frac{{\mathrm{cm}}}{{\mathrm{s}}})$')
plt.legend()
plt.rcParams.update({'font.size': 24})
plt.show()