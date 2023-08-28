import sys
import plotly.graph_objects as graph

def draw(iterations, dataOrder):
    fig = graph.Figure(
        data=graph.Scatter(
            x=list(range(1, iterations + 1)), y=dataOrder,
            mode='lines',
            marker={'color': 'pink'}
        ),
        layout=graph.Layout(
            xaxis=dict(title='Iteraciones', titlefont=dict(size=24)),
            yaxis=dict(title='Orden', titlefont=dict(size=24)),
            plot_bgcolor='rgba(0,0,0,0)'
        )
    )

    fig.update_layout(width=1250, height=1250)
    fig.update_xaxes(
        mirror=True,
        ticks='outside',
        showline=True,
        linecolor='black',
        tickfont=dict(size=24),
    )
    fig.update_yaxes(
        mirror=True,
        ticks='outside',
        showline=True,
        linecolor='black',
        tickfont=dict(size=24),
    )
    fig.show()


def read_input_file(filename):
    lines = []
    with open(filename) as order:
        lines = order.readlines()[2:]
    variations = []
    for line in lines:
        aux = line.split('\t')[1:2]
        variations.append(float(aux[0]))

    return len(variations), variations

if __name__ == '__main__':
    var, iterations = read_input_file("../output/va_Output_L5.txt")
    draw(var, iterations)