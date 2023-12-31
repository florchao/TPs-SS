import numpy as np
import pandas as pd
from ovito.data import DataCollection, SimulationCell, Particles
from ovito.io import export_file
from ovito.pipeline import StaticSource, Pipeline

def parseParams(frame_file):
    frames = []
    with open(frame_file, "r") as frame:
        next(frame)
        next(frame)
        frame_lines = []
        for line in frame:
            ll = line.split()
            line_info = []
            for index in ll:
                line_info.append(float(index))
            if len(line_info) > 1:
                frame_lines.append(line_info)
            elif len(line_info) == 1:
                df = pd.DataFrame(np.array(frame_lines), columns=["id", "x", "vx", "fx", "radius"])
                frames.append(df)
                frame_lines = []
        df = pd.DataFrame(np.array(frame_lines), columns=["id", "x", "vx", "fx", "radius"])
        frames.append(df)
    return frames


def get_particles(data_frame):
    particles = Particles()
    particles.create_property('Particle Identifier', data=data_frame.id)
    particles.create_property('Position', data=np.array((data_frame.x, np.zeros([len(data_frame.x,)]), np.zeros([len(data_frame.x),]))).T)
    particles.create_property('Radius', data=data_frame.radius)
    particles.create_property('Velocity', data=np.array((data_frame.vx, np.zeros([len(data_frame.vx,)]), np.zeros([len(data_frame.vx,)]))).T)

    return particles


if __name__ == '__main__':
    data_frame = parseParams("../output/positionN5K3.txt")
    pipeline = Pipeline(source=StaticSource(data=DataCollection()))

    def simulation_cell(frame, data):
        cell = SimulationCell()
        cell.is2D = True
        cell[:, 0] = (224, 0, 0)
        cell[:, 1] = (0, 112, 0)
        data.objects.append(cell)
        particles = get_particles(data_frame[frame])
        data.objects.append(particles)

    pipeline.modifiers.append(simulation_cell)

    export_file(pipeline, "../output/simulationK1" + '.dump', 'lammps/dump',
                columns=["Particle Identifier", "Position.X", "Velocity.X", "Radius"],
                multiple_frames=True, start_frame=0, end_frame=len(data_frame) - 1)
