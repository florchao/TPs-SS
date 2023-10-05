import os
import matplotlib.pyplot as plt
import numpy as np

A = 1.0
M = 70.0
K = 10000
GAMMA = 100.0


def calculate(t):
    return A * (np.exp(-(GAMMA / (2 * M)) * t)) * (np.cos(np.power((K / M) - (GAMMA * GAMMA / (4 * (M * M))), 0.5) * t))

def parseParams(file):
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


deltas= [0.00001, 0.00005, 0.0001, 0.0005, 0.001, 0.005]

errorsV = []
errorsG = []
errorsB = []

for filename in os.listdir("../output/"):
    file_path = os.path.join("../output/", filename)
    if os.path.isfile(file_path):
        if file_path.__contains__('Beeman'):
            errorsB.append(parseParams(file_path))
        elif file_path.__contains__('Gear'):
            errorsG.append(parseParams(file_path))
        else:
            errorsV.append(parseParams(file_path))

eV = np.sort(errorsV)
eG = np.sort(errorsG)
eB = np.sort(errorsB)

plt.plot(deltas, eV, "o-", label="Verlet", color = "pink")
plt.plot(deltas, eB, "o-", label="Beeman", color = "purple")
plt.plot(deltas, eG, "o-", label="Gear", color = "orange")

plt.yscale("log")
plt.xscale("log")
plt.legend()
plt.ylabel(r"Error cuadrático medio ($m^2$)")
plt.xlabel(r'$\Delta$t (s)')
plt.rcParams.update({'font.size': 24})
plt.show()

