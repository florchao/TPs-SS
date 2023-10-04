import matplotlib.pyplot as plt

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
            }

            particles_data[time].append(particle)

    return particles_data


ks = [1,2,3,4,5]
phi = {}
colors = ['purple', 'magenta', 'orange', 'cyan', 'red', 'lime']

index = 1
for dt in range(len(ks) - 1):
    name = ks[dt]
    nextName=ks[dt+1]
    currentData = parseParams("../output/realPositionN25" + "K" + str(name) + ".txt")
    nextData = parseParams("../output/realPositionN25K" + str(nextName) + ".txt")

    aux_phi = []
    for i, j in zip(currentData.keys(), nextData.keys()):
        diff = 0

        for current, next in zip(currentData[i], nextData[j]):
            diff += (abs(next['x'] - current['x']))
        aux_phi.append(diff)
    phi[index] = aux_phi
    plt.plot([i * 0.1 for i in range(0, 1801)], aux_phi, linestyle='-', color=colors[index ],label=f'K= {index}')
    index += 1

plt.xlabel('Tiempo (s)')
plt.ylabel('Φ (cm)')
plt.yscale('log')
plt.legend()
plt.rcParams.update({'font.size': 24})
plt.show()
