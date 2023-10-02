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


fileName = '../output/realPositionN25'
dt_values = [1.0E-1, 1.0E-2, 1.0E-3, 1.0E-4, 1.0E-5]
ks = [1,2,3,4,5]
phi_dt_difference = {}
colors = ['purple', 'pink', 'orange', 'lightgreen', 'lightblue', 'magenta']

index = 1
for dt in range(len(dt_values) - 1):
    name = ks[dt]
    nextName=ks[dt+1]
    currentData = parseParams(fileName + "K" + str(name) + ".txt")
    nextData = parseParams(fileName + "K" + str(nextName) + ".txt")

    aux_phi = []
    for i, j in zip(currentData.keys(), nextData.keys()):
        diff = 0

        for current, next in zip(currentData[i], nextData[j]):
            diff += (abs(next['x'] - current['x']))

        if name == 1:
            aux_phi.append(diff/1000000)
        elif name == 2:
            aux_phi.append(diff/15)
        elif name == 3:
            aux_phi.append(diff/70)
        elif name == 4:
            aux_phi.append(diff/10)

    phi_dt_difference[index] = aux_phi
    plt.plot([i * 0.1 for i in range(0, 1801)], aux_phi, linestyle='-', color=colors[index ],label=f'K= {index}')
    index += 1

plt.xlabel('Tiempo (s)')
plt.ylabel('Φ (cm)')
plt.legend()
plt.rcParams.update({'font.size': 24})
plt.show()
