import matplotlib.pyplot as plt
from matplotlib import animation

def parse_output_file(file_path):
    with open(file_path, 'r') as file:
        lines = file.readlines()

    # Inicializa las variables para almacenar temporalmente los datos
    particles_data = {}
    time = None

    for line in lines:
        data = line.split("\t")
        # data = line.split()

        if len(data) == 1:
            time = float(data[0])
            particles_data[time] = []
        else:
            particle = {
                'x': float(data[0]),
                'y': float(data[1]),
                'vx': float(data[2]),
                'vy': float(data[3]),
                'radius': float(data[4]),
                'id': int(data[5])
            }

            particles_data[time].append(particle)

    return particles_data

def update_particle_for_simulation(frame, particle_data, ax2d, main_height, main_width, minor_height, minor_width):
    # Esta línea obtiene una lista de todas las claves del diccionario events, convierte esa lista en una lista ordenada, y luego selecciona la clave en la posición frame. Esto significa que t ahora contiene la clave correspondiente al paso en la animación que se debe representar en el gráfico.
    t = list(particle_data.keys())[frame]
    particles = particle_data[t]

    ax2d.clear()

    ax2d.plot([0, 0], [0, main_height], color='b')  # 0
    ax2d.plot([0, main_width], [0, 0], color='b')  # 1
    ax2d.plot([main_width, main_width], [0, (main_height - minor_height) / 2], color='b')  # 2
    ax2d.plot([main_width, main_width + minor_width], [(main_height - minor_height) / 2, (main_height - minor_height) / 2], color='b')  # 3
    ax2d.plot([main_width + minor_width, main_width + minor_width], [(main_height - minor_height) / 2, (main_height - minor_height) / 2 + minor_height], color='b')  # 4
    ax2d.plot([main_width + minor_width, main_width], [(main_height - minor_height) / 2 + minor_height, (main_height - minor_height) / 2 + minor_height], color='b')  # 5
    ax2d.plot([main_width, main_width], [(main_height - minor_height) / 2 + minor_height, main_height], color='b')  # 6
    ax2d.plot([main_width, 0], [main_height, main_height], color='b')  # 7

    ax2d.set_xticks([0, main_width, main_width + minor_width])
    ax2d.set_yticks([0, (main_height - minor_height) / 2, (main_height - minor_height) / 2 + minor_height, main_height])

    for p in particles:
        cir = plt.Circle((p['x'], p['y']), p['radius'], color='r', fill=True)
        ax2d.set_aspect('equal', adjustable='datalim')
        ax2d.add_patch(cir)

        # Agregar el ID de la partícula como un texto en el centro del círculo
        ax2d.annotate(str(p['id']), xy=(p['x'], p['y']), fontsize=10, ha='center', va='center')

    # ax2d.scatter(([p['x'] for p in particles]), ([p['y'] for p in particles]), s=75, c='b')

    ax2d.set_xlabel('Position X')
    ax2d.set_ylabel('Position Y')
    ax2d.set_title(f'Time: ' + str(t))

def main():
    output_file_path = "../files/output.txt"

    # Crea la figura y el eje
    fig = plt.figure()
    ax2d = fig.add_subplot(111)

    width = 0.09 
    L = 0.03
    particle_data = parse_output_file(output_file_path)

    # Llama a la función de actualización de la trama
    anim = animation.FuncAnimation(fig, update_particle_for_simulation, fargs=(particle_data, ax2d, width, width, L, width), frames=len(particle_data))
    anim.save('animation.mp4', writer='ffmpeg', fps=10)

if __name__ == "__main__":
    main()