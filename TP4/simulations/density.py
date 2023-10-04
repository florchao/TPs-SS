from matplotlib import pyplot as plt
import numpy as np

dt_value = 1.0E-3
N_values = [5,10,15,20,25,30]
map_for_density = []
map_for_velocity = []
colors = ['purple', 'pink', 'orange', 'lightgreen', 'lightblue', 'magenta']

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
                'x': float(data[1]),
                'vx': float(data[2]),
            }

            particles_data[time].append(particle)

    return particles_data

for N in N_values:
    map_for_density = []
    map_for_velocity = []
    current_dt = dt_value
    current_dt_particle_data = parseParams("../output/positionN" + str(N) +"K3.txt")

    # Calcula la diferencia entre los datos de partículas y almacénala en phi_dt_difference
    aux_density = []
    aux_velocity = []
    for time in current_dt_particle_data.keys():
        particles_list = current_dt_particle_data[time]
        for current_particle_index in range(len(particles_list)):
            density_value = 0
            velocity_value = 0
            current_particle = particles_list[current_particle_index]

            if current_particle_index > 0:
                previous_particle = particles_list[current_particle_index - 1]
            else:  # Caso de que sea la primer particula, la anterior es la ultima particula
                previous_particle = particles_list[-1]

            if current_particle_index < len(particles_list) - 1:
                next_particle = particles_list[current_particle_index+1]
            else:  # Caso de que sea la ultima particula, la sigueinte seria la primer particula
                next_particle = particles_list[0]

            density_value = 1/((abs(current_particle['x'] - previous_particle['x'])) + (abs(current_particle['x'] - next_particle['x'])))
            velocity_value = current_particle['vx']
            map_for_density.append(density_value)
            map_for_velocity.append(velocity_value)

for i, N in enumerate(N_values):
    window_size = 500
    smoothed_density = np.convolve(map_for_density, np.ones(window_size)/window_size, mode='valid')
    smoothed_velocity = np.convolve(map_for_velocity, np.ones(window_size)/window_size, mode='valid')

plt.plot(smoothed_velocity, smoothed_density, color=colors[i])


plt.ylabel('Velocidad ($\\frac{{\mathrm{cm}}}{{\mathrm{s}}})$')
plt.xlabel('Densidad ($\\frac{{\mathrm{cm}}}{{\mathrm{s}}})$')
plt.legend(scatterpoints=1, markerscale=5)
plt.show()