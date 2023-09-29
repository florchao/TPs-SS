import matplotlib.pyplot as plt
import numpy as np
import math

N=[200]
L=[0.03]

def proccesing(particles, interval):

    with open('../files/output/finalPressure200L0.03.txt', 'r') as file:
        t0 =  float(file.readline().split()[1])
    with open("../files/output/simulation200L0.03.dump", 'r') as file:
        lines = file.readlines()

    displacements = {}
    base = []
    found = False

    for i in range(1, len(lines), ((1080 + 2) + particles) * interval):
        frame_info = lines[i].split()
        frame_number = float(frame_info[1])
        if t0 > frame_number:
            continue
        data = []
        frame_DCM = 0
        frame_std = []

        for j in range(i + 1, i + particles + 1):
            parts = lines[j].split()
            id, x, y, _, _, _, _, _, _ = map(float, parts)
            if 1 <= id <= particles:
                data.append((id, x, y))
            if not found:
                base.append((id, x, y))
        found = True

        for id, x, y in data:
            displacement = ((x - base[int(id) - 1][1])**2 + (y - base[int(id) - 1][2])**2)**0.5
            frame_DCM += displacement**2
            frame_std.append(displacement**2)

        DCM = frame_DCM/particles

        displacements[frame_number] = {"DCM": DCM, "STD": np.std(frame_std)}

    return displacements

def dcmValues(values, tolerance):
    mean = np.mean(values[len(values)//2:])
    for i in range(3*len(values)//4):
        if abs(values[i] - mean) < tolerance:
            return values[0:i+1], i + 1
    return dcmValues(values, tolerance * 10)


def diffCoeff(DCM_values, frames):

    real_values = dcmValues(DCM_values, 0.0001)

    tiempo = np.arange(len(real_values))
    
    f1 = frames[:len(tiempo)][0]
    x = [item for item in frames[:len(tiempo)]]
    c = [x * 0.000001 for x in range(-100, 100)]
    ec = []
    for ci in c:
        ec.append(sum([math.pow(yi - ci*xi, 2) for xi, yi in zip(x, real_values)]))
    for(eci, ci) in zip(ec, c):
        if eci == min(ec):
            pendiente = ci
    x_labels = []
    for frame in c:
        x_labels.append(f'{frame - frames[0]:.2f}')

    

    f1 = frames[:len(tiempo)][0]
    x = [item - f1 for item in frames[:len(tiempo)]]
    c = [x * 0.00001 for x in range(-500, 500)]
    ec = []
    for ci in c:
        ec.append(sum([math.pow(yi - ci*xi, 2) for xi, yi in zip(x, real_values)]))
    for(eci, ci) in zip(ec, c):
        if eci == min(ec):
            pendiente = ci

    return pendiente / 4

def plot(dictionary, N_particles, Lsize, version):
    frames = list(dictionary.keys())
    DCM_values = [value['DCM'] for value in dictionary.values()]
    STD_errors = [value['STD'] for value in dictionary.values()]

    x = np.arange(len(frames))

    ax = plt.subplots(figsize=(15, 9))
    ax.errorbar(x[1:], DCM_values[1:], yerr=STD_errors[1:], fmt='o', color='magenta', label='STD')

    x_labels = []
    for frame in frames:
        x_labels.append(f'{frame - frames[0]:.2f}')

    ax.set_xlabel('Tiempo (s)')
    ax.set_ylabel("Desplazamiento Cuadrático Medio (m^2)")
    j = 0
    xLabels = []
    xPos = []
    while j < len (x):
        xLabels.append(x[j])
        xPos.append(j)
        j+=10
    plt.xticks(xPos, xLabels)

    D = diffCoeff(DCM_values, frames, N_particles, Lsize, version)

    plt.savefig(f'../files/output/DCMvsTime_N_{N_particles}_L_{Lsize}_D_{D:f}.png')
    plt.close()
    return D


displacements = proccesing(considered_particles=N[0], interval=10)
plot(displacements, N[0], L[0], 1)