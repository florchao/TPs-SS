import matplotlib.pyplot as plot
import numpy
from statistics import mean


def draw(orders1, orders2, orders3, orders4, orders5, orders6):

    x = [0.1, 1, 2, 3, 4, 5]
    y = [orders1[0], orders2[0], orders3[0], orders4[0], orders5[0], orders6[0]]
    yerr = [orders1[1], orders2[1], orders3[1], orders4[1], orders5[1], orders6[1]]

    fig, ax = plot.subplots()

    ax.errorbar(x, y, yerr, fmt='o', linewidth=3, capsize=12, color="pink", ecolor='purple', elinewidth=3)

    ax.set(xlim=(0, 5.3), xticks=numpy.arange(0, 5.3, 1),
           ylim=(0, 1.1), yticks=numpy.arange(0, 1.1, 0.2))

    plot.xlabel('Ruido')
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
    orders1 = read_input_file("../output/va_Output_order0.1.txt")
    orders2 = read_input_file("../output/va_Output_order1.txt")
    orders3 = read_input_file("../output/va_Output_order2.txt")
    orders4 = read_input_file("../output/va_Output_order3.txt")
    orders5 = read_input_file("../output/va_Output_order4.txt")
    orders6 = read_input_file("../output/va_Output_order5.txt")
    draw(orders1, orders2, orders3, orders4, orders5, orders6)