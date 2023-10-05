import matplotlib.pyplot as plt
import numpy as np

def calculate(t):
    return 1.0 * (np.exp(-(100.0 / (2 * 70.0)) * t)) * (np.cos(np.power((10000 / 70.0) - (100.0 * 100.0 / (4 * (70.0 * 70.0))), 0.5) * t))

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
    return r, position, time, error

rV, positionV, timeV, errorV = parseParams("../output/Verlet_1.txt")
rG, positionG, timeG, errorG = parseParams("../output/Gear_1.txt")
rB, positionB, timeB, errorB = parseParams("../output/Beeman_1.txt")

plt.plot(timeV, positionV, color = "magenta")
plt.plot(timeB, positionB, color = "purple", linestyle='dashed')
plt.plot(timeG, positionG, color = "orange")
plt.plot(timeV, rV, color = "lime", linestyle='dashdot')
plt.xlabel("Tiempo (s)")
plt.ylabel("Posición (m)")
plt.legend(["Verlet", "Beeman", "Gear", "Analítico"])
plt.rcParams.update({'font.size': 24})
plt.show()
