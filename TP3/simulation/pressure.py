import matplotlib.pyplot as plt

def plot_pressure(N_particles, L_size, v):
    for N in N_particles:
        for L in L_size:
            x_values = []
            y1_values = []
            y2_values = []

            with open('../files/output/pressures' + str(N) +'.txt', 'r') as file:
                for line in file:
                    parts = line.strip().split()
                    if len(parts) == 3:
                        x_values.append(float(parts[0]))
                        y1_values.append(float(parts[1]))
                        y2_values.append(float(parts[2]))

            # Create the plot
            plt.figure(figsize=(8, 6))
            plt.plot(x_values, y1_values, label='Left side')
            plt.plot(x_values, y2_values, label='Right side')
            plt.xlabel('Time')
            plt.ylabel('Pressure')
            plt.legend()
            plt.title('Pressure')
            plt.grid(True)
            plt.savefig(f'../files/output/PressureTime{N}.png')
            plt.close()

N=[200]
L=[0.03, 0.05, 0.07, 0.09]

plot_pressure(N, L, 1)