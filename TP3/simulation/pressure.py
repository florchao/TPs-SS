import matplotlib.pyplot as plt

def plot_pressure():
    x_values = []
    y1_values = []
    y2_values = []

    with open('../files/output/pressures200L0.09.txt', 'r') as file:
        currentTime = 0.0
        for line in file:
            parts = line.strip().split()
            if len(parts) == 3:
                newTime = round(float(parts[0]))
                if newTime > currentTime:
                    currentTime = newTime
                    x_values.append(float(parts[0]))
                    y1_values.append(float(parts[1]))
                    y2_values.append(float(parts[2]))
                
    plt.figure(figsize=(8, 6))
    plt.plot(x_values, y1_values, label='Left side', color="cyan")
    plt.plot(x_values, y2_values, label='Right side', color="magenta")
    plt.xlabel('Tiempo (s)')
    plt.ylabel('Presión (kg / (m*s^2))')
    plt.legend()
    plt.savefig(f'../files/output/PressureTime200L0.09.png')
    plt.close()

N=[200]
L=[0.09]

plot_pressure()
