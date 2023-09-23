import os
import matplotlib.pyplot as plt
import numpy as np

A = 1.0
M = 70.0
K = 10000
GAMMA = 100.0


def calculate(t):
    return A * (np.exp(-(GAMMA / (2 * M)) * t)) * (np.cos(np.power((K / M) - (GAMMA * GAMMA / (4 * (M * M))), 0.5) * t))


def draw(deltas, errorsV, errorsG, errorsB):

    plt.plot(deltas, errorsV, "o-", label="Verlet", color = "pink")
    plt.plot(deltas, errorsB, "o-", label="Beeman", color = "purple")
    plt.plot(deltas, errorsG, "o-", label="Gear", color = "orange")

    plt.yscale("log")
    plt.xscale("log")
    plt.legend()
    plt.ylabel("Error cuadrático medio (m^2)")
    plt.xlabel("Deltas (s)")
    plt.rcParams.update({'font.size': 22})
    plt.show()


def parse_parameters(file):
    with open(file) as statesFile:
        statesLines = statesFile.readlines()

    r = []
    time = []
    position = []
    error = 0
    for line in statesLines:
        newline = line.strip()
        split = newline.split()
        if len(split) == 3:
            aux = calculate(float(split[0]))
            r.append(aux)
            position.append(float(split[1]))
            time.append(float(split[0]))

    for x, y in zip(r, position):
        error = error + (y - x) ** 2
    return error


if __name__ == '__main__':
    deltas= [0.00001, 0.00005, 0.0001, 0.0005, 0.001, 0.005]

    errorsV = []
    errorsG = []
    errorsB = []

    directory = "../output/"

    num_files = 0

    for filename in os.listdir(directory):
        file_path = os.path.join(directory, filename)
        if os.path.isfile(file_path):
            num_files += 1
            if file_path.__contains__('Beeman'):
                errorsB.append(parse_parameters(file_path))
            elif file_path.__contains__('Gear'):
                errorsG.append(parse_parameters(file_path))
            else:
                errorsV.append(parse_parameters(file_path))

    eV = np.sort(errorsV)
    eG = np.sort(errorsG)
    eB = np.sort(errorsB)

    draw(deltas, eV, eG, eB)

