import sys

import pandas
import numpy
from ovito.pipeline import StaticSource, Pipeline
from ovito.data import DataCollection, SimulationCell, Particles
from ovito.io import export_file


def generate_file(staticFile, iterations):
    pipeline = Pipeline(source=StaticSource(data=DataCollection()))
    with open(staticFile, "r") as f:
        next(f)
        L = f.readline()

    def simCell(frame, data):
        cell = SimulationCell()
        cell.is2D= True
        cell[:, 0] = (L, 0, 0)
        cell[:,1] = (0,L,0)
        data.objects.append(cell)
        particles = fetchParticles(iterations[frame])
        data.objects.append(particles)
    pipeline.modifiers.append(simCell)

    export_file(pipeline, 'sim.dump', 'lammps/dump',
                columns=["Particle Identifier", "Position.X", "Position.Y", "Velocity.X", "Velocity.Y"],
                multiple_frames=True, start_frame=0, end_frame=len(iterations) - 1)



def fetchParticles(frame):
    particles = Particles()
    particles.create_property('Particle Identifier', data = frame.id)
    particles.create_property('Position', data=numpy.array((frame.x, frame.y, numpy.zeros([len(frame.x), ]))).T)
    particles.create_property('Angle', data=frame.theta)
    particles.create_property('Velocity', data=numpy.array((numpy.cos(frame.theta) * frame.v,
                                                            numpy.sin(frame.theta) * frame.v,
                                                            numpy.zeros([len(frame.x, )]))).T)

    return particles

def read_input_file(filename):
    iterations = []
    with open(filename, "r") as iteration:
        next(iteration)
        lines = []
        for line in iteration:
            parts = line.split()
            info=[]
            for index in parts:
                info.append(float(index))
            if len(info) == 1:
                dataFrame= pandas.DataFrame(numpy.array(lines), columns = ["id", "x", "y", "v", "theta"])
                iterations.append(dataFrame)
                lines = []
            elif len(info) > 1:
                lines.append(info)
    dataFrame= pandas.DataFrame(numpy.array(lines), columns = ["id", "x", "y", "v", "theta"])
    iterations.append(dataFrame)

    return iterations

if __name__ == "__main__":
    input_filename = "../output/dynamicOutput.txt"
    static_filename = "../output/static.txt"

    iterations = read_input_file(input_filename)
    generate_file(static_filename, iterations)
