import matplotlib.pyplot as plt
import numpy as np

A = 1.0
m = 70.0
k = 10000
gamma = 100.0


def calculate(t):
    return A * (np.exp(-(gamma / (2 * m)) * t)) * (np.cos(np.power((k / m) - (gamma * gamma / (4 * (m * m))), 0.5) * t))


def draw(rV, positionV, timeV, rG, positionG, timeG, rB, positionB, timeB):

    plt.plot(timeV, positionV, color = "pink")
    plt.plot(timeB, positionB, color = "purple", linestyle='dashed')
    plt.plot(timeG, positionG, color = "orange")
    plt.plot(timeV, rV, color = "green", linestyle='dashdot')
    plt.xlabel("Tiempo (s)")
    plt.ylabel("Posición (m)")
    plt.legend(["Verlet", "Beeman", "Gear", "Analítico"])
    plt.rcParams.update({'font.size': 24})
    plt.show()


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


if __name__ == '__main__':
    rV, positionV, timeV, errorV = parseParams("../output/Verlet_1.txt")
    rG, positionG, timeG, errorG = parseParams("../output/Gear_1.txt")
    rB, positionB, timeB, errorB = parseParams("../output/Beeman_1.txt")
    draw(rV, positionV, timeV, rG, positionG, timeG, rB, positionB, timeB)
    print("Error cuadratico Verlet: ", errorV)
    print("Error cuadratico Gear: ", errorG)
    print("Error cuadratico Beeman: ", errorB)
