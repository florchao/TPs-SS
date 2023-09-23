import sys
import matplotlib.pyplot as plt
import numpy as np

A = 1.0
m = 70.0
k = 10000
gamma = 100.0


def calculate(t):
    return A * (np.exp(-(gamma / (2 * m)) * t)) * (np.cos(np.power((k / m) - (gamma * gamma / (4 * (m * m))), 0.5) * t))


def drawAll(verlet, gear, beeman):
    positionsVerlet = np.array(verlet[1])
    timesVerlet = np.array(verlet[2])
    positionsBeeman = np.array(beeman[1])
    timesBeeman = np.array(beeman[2])
    positionsGear = np.array(gear[1])
    timesGear = np.array(gear[2])
    real_positions = np.array(verlet[0])

    plt.plot(timesVerlet, positionsVerlet, color = "pink")
    plt.plot(timesBeeman, positionsBeeman, color = "purple")
    plt.plot(timesGear, positionsGear, color = "orange")
    plt.plot(timesVerlet, real_positions, color = "green")
    plt.xlabel("Tiempo (s)")
    plt.ylabel("Posicion (m)")
    plt.legend(["Verlet", "Gear", "Beeman", "Analitico"])
    plt.rcParams.update({'font.size': 22})
    plt.show()


def parseParameters(file):
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


if __name__ == '__main__':
    r, position, time, errorV = parseParameters("../output/Verlet_1.txt")
    r2, position2, time2, errorG = parseParameters("../output/Gear_1.txt")
    r3, position3, time3, errorB = parseParameters("../output/Beeman_1.txt")
    drawAll([r, position, time], [r2, position2, time2], [r3, position3, time3])
    print("Error cuadratico Verlet: ", errorV)
    print("Error cuadratico Gear: ", errorG)
    print("Error cuadratico Beeman: ", errorB)
