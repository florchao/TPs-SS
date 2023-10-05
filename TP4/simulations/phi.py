import matplotlib.pyplot as plt

def parseParams(file_path):
    with open(file_path, 'r') as file:
        lines = file.readlines()

    particles = {}
    time = 0
    for line in lines:
        data = line.split("\t")
        if len(data) == 1:
            time = float(data[0])
            particles[time] = []
        else:
            particle = {
                'x': float(data[1]),
            }

            particles[time].append(particle)

    return particles


ks = [1,2,3,4,5]
colors = ['purple', 'magenta', 'orange', 'cyan', 'red', 'lime']

index = 1
for k in range(len(ks) - 1):
    currentData = parseParams("../output/realPositionN25K" + str(ks[k]) + ".txt")
    nextData = parseParams("../output/realPositionN25K" + str(ks[k+1]) + ".txt")

    aux = []
    for i, j in zip(currentData.keys(), nextData.keys()):
        diff = 0
        for current, next in zip(currentData[i], nextData[j]):
            diff += (abs(next['x'] - current['x']))
        aux.append(diff)
    plt.plot([i * 0.1 for i in range(0, 1801)], aux, linestyle='-', color=colors[k],label=f'K= {k}')
    index += 1

plt.xlabel('Tiempo (s)')
plt.ylabel('Φ (cm)')
plt.yscale('log')
plt.legend()
plt.rcParams.update({'font.size': 24})
plt.show()
