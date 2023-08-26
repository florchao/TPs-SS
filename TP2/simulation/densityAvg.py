import matplotlib.pyplot as plot
import numpy
import sys
from statistics import mean

def draw(orders1, orders2, orders3, orders4, orders5, orders6):
    N = 500

    x = [N/(5*5), N/(7*7), N/(10*10), N/(15*15), N/(20*20), N/(25*25)]
    y = [orders1[0], orders2[0], orders3[0], orders4[0], orders5[0], orders6[0]]
    yerr = [orders1[1], orders2[1], orders3[1], orders4[1], orders5[1], orders6[1]]


    fig, ax = plot.subplots()

    ax.errorbar(x, y, yerr, fmt='o', linewidth=3, capsize=12, color="pink", ecolor='purple', elinewidth=3)

    ax.set(xlim=(0, 14), xticks=numpy.arange(0, 14, 2),
           ylim=(0, 1.1), yticks=numpy.arange(0, 1.1, 0.2))

    plot.xlabel('Densidad')
    plot.ylabel('Orden')

    plot.show()


def read_input_file(filename):
    lines = []
    with open(filename) as order:
        lines = order.readlines()[2:]
    orders = []
    for line in lines:
        aux = line.split('\t')[1:2]
        orders.append(float(aux[0]))

    average = mean(orders[50:])
    deviation = numpy.std(orders[50:])
    return [average, deviation]

if __name__ == '__main__':
    orders1 = read_input_file(sys.argv[1])
    orders2 = read_input_file(sys.argv[2])
    orders3 = read_input_file(sys.argv[3])
    orders4 = read_input_file(sys.argv[4])
    orders5 = read_input_file(sys.argv[3])
    orders6 = read_input_file(sys.argv[4])

    draw(orders1, orders2, orders3, orders4, orders5, orders6)
