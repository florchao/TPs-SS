import os
import glob
import numpy as np
import matplotlib.pyplot as plt

# Define the values of N and L
N_values = [200,210, 220, 230, 240, 250]
L_values = [0.03, 0.05, 0.07, 0.09]
file_list = []
color_list = ['b', 'g', 'r', 'c', 'm', 'y']
# Create a dictionary to store data for each combination of N and L
data = {}



for iteration in range(len(N_values)):
    file_list = []
    for i in range(len(L_values)):
        # format noise[i] to exactly 2 decimals
        file_list.append(f"../files/output/finalPressure{N_values[iteration]}.txt")
    # Loop through each file and read data
    pressure_perL=[]
    pressure=[]
    stds=[]
    areas =[]
    area = 0.09*0.09 + 0.09*L_values[i]
    for idx, file_name in enumerate(file_list):
        pressure=[]
        with open(file_name, "r") as file:
            # vas.append(0)
            # _per_error=[]
            lines = file.readlines()
            for line in lines:
                pressure.append( float(line.strip().split()[0]))
                # va_per_error = (pressure)
            pressure_perL.append(np.mean(pressure))
            stds.append(np.std(pressure))
            areas.append(1/area)
    # print(N[iteration], L[iteration], len(noise), (vas))
    plt.scatter(areas, pressure_perL, marker='o', linestyle='-', color=color_list[iteration], label=f'L= {N_values[iteration]} ')
    plt.errorbar(areas, pressure_perL, yerr=stds, fmt='o', color=color_list[iteration], ecolor=color_list[iteration], capthick=2)
    pressurre=[]
    stds=[]

plt.xlabel('Inversa del Área')
plt.ylabel('Presion')
plt.legend()
plt.grid(True)
plt.savefig('../files/output/finalPressure.png')

plt.cla()